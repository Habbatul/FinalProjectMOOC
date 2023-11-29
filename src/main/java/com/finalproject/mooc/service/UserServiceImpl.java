package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.ResetPassword;
import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.enums.ERole;
import com.finalproject.mooc.model.requests.ResetPasswordRequest;
import com.finalproject.mooc.model.requests.UpdateUserPassword;
import com.finalproject.mooc.model.requests.UpdateUserRequest;
import com.finalproject.mooc.model.responses.UserResponse;
import com.finalproject.mooc.repository.ResetPasswordRepository;
import com.finalproject.mooc.repository.UserRepository;
import com.finalproject.mooc.service.media.CloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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


    @Transactional
    @Override
    public void updateProfile(String username, UpdateUserRequest userRequest) throws IOException {
        String imageUrl;

        User user = userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan"));

        if(userRequest.getFile() != null) {
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
                    .phoneNumber((userRequest.getPhoneNumber() != null) ? userRequest.getPhoneNumber() : user.getPhoneNumber())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .build();

        userRepository.save(saveuser);
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

    @Transactional
    @Override
    public void resetPassword(String resetToken, ResetPasswordRequest passwordRequest) {
        ResetPassword findToken = resetPasswordRepository.findToken(resetToken).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token tidak ditemukan atau sudah kadaluarsa"));
        LocalDateTime time = findToken.getTime();
        LocalDateTime now = LocalDateTime.now();

        //cek waktu token dengan waktu sekarang apakah kurang dari 1 menit
        if(Duration.between(time, now).toMinutes() < 1){
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
    public String makeTokenResetPassword(String emailAddress) {
        ResetPassword token = ResetPassword.builder()
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .user(userRepository.findUserByEmailAddress(emailAddress).orElseThrow(
                        ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username tidak ditemukan")))
                .build();
        resetPasswordRepository.save(token);

        //lakukan logic untuk kirim email disini kirimkan url Front-End dan berikan parameter token
        //example : <a href= www.example.com/forget-password/{resetToken}>Tekan disini untuk reset password</a>

        return token.getToken();
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse showUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan"));

        return toUserResponse(user);
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

}
