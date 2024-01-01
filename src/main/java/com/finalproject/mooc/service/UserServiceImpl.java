package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.RegisterOtp;
import com.finalproject.mooc.entity.ResetPassword;
import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.model.requests.ResetPasswordRequest;
import com.finalproject.mooc.model.requests.UpdateUserPassword;
import com.finalproject.mooc.model.requests.UpdateUserRequest;
import com.finalproject.mooc.model.responses.UserResponse;
import com.finalproject.mooc.repository.RegisterOtpRepository;
import com.finalproject.mooc.repository.ResetPasswordRepository;
import com.finalproject.mooc.repository.UserRepository;
import com.finalproject.mooc.security.JwtUtil;
import com.finalproject.mooc.service.media.CloudinaryService;
import com.finalproject.mooc.service.security.UserDetailsServiceImpl;
import com.finalproject.mooc.util.EmailUtil;
import com.finalproject.mooc.util.OtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private RegisterOtpRepository registerOtpRepository;

    //untuk generate token baru setelah ubah password dan profile
    @Autowired
    JwtUtil jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public String updateProfile(String username, UpdateUserRequest userRequest) throws IOException {
        String imageUrl;

        User user = userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan"));

        Optional<MultipartFile> userRequestFile = Optional.ofNullable(userRequest.getFile());
        if(userRequestFile.isPresent()) {
            if(user.getUrlPhoto() != null) cloudinaryService.deleteFile(user.getUrlPhoto());
            imageUrl = cloudinaryService.uploadFile(userRequest.getFile());
        } else{
            imageUrl=null;
        }

        User saveuser = User.builder()
                    .userId(user.getUserId())
                    .urlPhoto(imageUrl != null ? imageUrl : user.getUrlPhoto())
                    .username((userRequest.getUsername() != null && !userRequest.getUsername().isEmpty()) ? userRequest.getUsername() : user.getUsername())
                    .emailAddress((userRequest.getEmailAddress() != null && !userRequest.getEmailAddress().isEmpty()) ? userRequest.getEmailAddress() : user.getEmailAddress())
                    .city((userRequest.getCity() != null && !userRequest.getCity().isEmpty()) ? userRequest.getCity() : user.getCity())
                    .country((userRequest.getCountry() != null && !userRequest.getCountry().isEmpty()) ? userRequest.getCountry() : user.getCountry())
                    .phoneNumber((userRequest.getPhoneNumber() != null && !userRequest.getPhoneNumber().isEmpty()) ? userRequest.getPhoneNumber() : user.getPhoneNumber())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .isActive(user.getIsActive())
                    .build();

        userRepository.save(saveuser);

        //membuat objek Authentication baru dengan username yang diubah, disini token berubah
        if ((userRequest.getUsername() != null && !userRequest.getUsername().isEmpty())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(saveuser.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if(!user.getUsername().equals(userRequest.getUsername())) return "Username berubah, pastikan update token";
        }


        return "Username tidak berubah, tidak perlu merubah token";
    }

    @Transactional
    @Override
    public void updatePassword(String username, UpdateUserPassword passwordRequest) {
        User user = userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username tidak ditemukan"));

        //bandingkan hash keduanya apakah sama
        if(!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password lama anda salah");
        }

        if(passwordRequest.getNewPassword().equals(passwordRequest.getNewRePassword()))
            userRepository.updatePasswordByUserId(user.getUserId(), passwordEncoder.encode(passwordRequest.getNewPassword()));
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RePassword tidak cocok");
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse showUserByUsername(String username) {
        log.info("showUserByUsername, String username = {}", username);
        User user = userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan"));

        return toUserResponse(user);
    }

    @Override
    public String verifyAccount(String email, String otp) {
        User user = userRepository.findUserByEmailAddress(email)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found with this email: " + email));

        if (user.getRegisterOtp().get(0).getOtp().equals(otp) &&
                Duration.between(user.getRegisterOtp().get(0).getOtpGenerateTime(), LocalDateTime.now()).getSeconds() < (4 * 60)) {

            user.setIsActive(true);
            userRepository.save(user);

            return "OTP verified, you can log in";
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please regenerate OTP and try again");
    }

    /*
    * Method ini masih error karena data otp ditable register otp tidak terupdate tetapi malah tambah data baru,
    * sehingga waktu verify otp yang baru tidak terdeteksi karena get pada method verifyAccount mengambil data ke-0
    * */
    @Override
    public String regenerateOtp(String email) {
        User user = userRepository.findUserByEmailAddress(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found with this email: " + email));

        String otp = otpUtil.generateOtp();
        RegisterOtp registerOtp = new RegisterOtp();
        registerOtp.setOtp(otp);
        registerOtp.setOtpGenerateTime(LocalDateTime.now());
        user.addRegisterOtp(registerOtp);

        log.info("waktu pada otp seharusnya : {} ", registerOtp.getOtpGenerateTime());

        String oldOtp = registerOtpRepository.getOtpOld(email);
        registerOtpRepository.updatePasswordByOtp(oldOtp, registerOtp);

        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to send OTP please try again : " + e.getMessage());
        }


        userRepository.save(user);
        return "Email sent... please verify account withing 2 minute";
    }

    private UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .imageUrl(user.getUrlPhoto())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .emailAddress(user.getEmailAddress())
                .country(user.getCountry())
                .city(user.getCity())
                .build();
    }

    //bagian dibawah ini untuk reset password
    @Transactional
    @Override
    public void resetPassword(ResetPasswordRequest passwordRequest) {
        ResetPassword findToken = resetPasswordRepository.findToken(passwordRequest.getResetToken()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token tidak ditemukan atau sudah kadaluarsa"));
        LocalDateTime time = findToken.getTime();
        LocalDateTime now = LocalDateTime.now();

        //cek waktu token dengan waktu sekarang apakah kurang dari 1 menit
        if(Duration.between(time, now).toMinutes() < 30){
            User user = userRepository.findUserByUsername(findToken.getUser().getUsername()).orElseThrow(
                    ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username tidak ditemukan"));

            if(passwordRequest.getNewPassword().equals(passwordRequest.getNewRePassword())){
                userRepository.updatePasswordByUserId(user.getUserId(), passwordEncoder.encode(passwordRequest.getNewPassword()));
                //hapus token seetelah berhasil update password
                resetPasswordRepository.delete(findToken);
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RePassword tidak cocok");
            }
        } else{
            //hapus token bila durasi telah habis
            resetPasswordRepository.delete(findToken);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Durasi form reset password telah berakhir");
        }
    }


    @Transactional
    @Override
    public void makeTokenResetPassword(String emailAddress) {
        ResetPassword token = ResetPassword.builder()
                .time(LocalDateTime.now())
                .user(userRepository.findUserByEmailAddress(emailAddress).orElseThrow(
                        ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email tidak ditemukan")))
                .build();
        resetPasswordRepository.save(token);

        //lakukan logic untuk kirim email disini kirimkan url Front-End dan berikan parameter token
        //example : <a href= www.example.com/forget-password/{resetToken}>Tekan disini untuk reset password</a>

        try {
            //nantinya bisa ganti url
            emailUtil.sendOtpEmailResetPassword(emailAddress, token.getToken());
        } catch (MessagingException e) {
            log.info("Unable to send url please try again : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tidak bisa mengirim url, coba lagi");
        }
    }

}
