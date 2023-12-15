package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.RegisterOtp;
import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.entity.security.Roles;
import com.finalproject.mooc.enums.ERole;
import com.finalproject.mooc.model.requests.auth.LoginRequest;
import com.finalproject.mooc.model.requests.auth.SignupRequest;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.model.responses.auth.JwtResponse;
import com.finalproject.mooc.model.security.UserDetailsImpl;
import com.finalproject.mooc.repository.RegisterOtpRepository;
import com.finalproject.mooc.repository.RoleRepository;
import com.finalproject.mooc.repository.UserRepository;
import com.finalproject.mooc.security.JwtUtil;
import com.finalproject.mooc.util.EmailUtil;
import com.finalproject.mooc.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegisterOtpRepository registerOtpRepository;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private JwtUtil jwtUtils;

    @Transactional(readOnly = true)
    @Override
    public JwtResponse authenticateUser(LoginRequest login, HttpServletResponse response) {
        //sementara gini dulu
        User user = userRepository.findUserByEmailAddress(login.getEmailAddress())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User dengan email tidak ditemukan"));

        if (user.getIsActive() == null || !user.getIsActive() ) {
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User belum terverifikasi");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), login.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), roles);
    }

    @Transactional
    @Override
    public WebResponse<String> registerUser(SignupRequest signupRequest) {
        Boolean usernameExist = userRepository.existsByUsername(signupRequest.getUsername());
        if(Boolean.TRUE.equals(usernameExist)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username telah ada");
        }

        Boolean emailExist = userRepository.existsByEmailAddress(signupRequest.getEmail());
        if(Boolean.TRUE.equals(emailExist)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email telah ada");
        }

        Boolean phoneExist = userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber());
        if(Boolean.TRUE.equals(phoneExist)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nomor telepon telah ada");
        }

        User user = User.builder().username(signupRequest.getUsername()).
                emailAddress(signupRequest.getEmail()).
                password(passwordEncoder.encode(signupRequest.getPassword())).
                phoneNumber(signupRequest.getPhoneNumber()).
//                roles(new HashSet<>(Collections.singletonList(Roles.builder().roleName(ERole.USER).build()))).
                build();


        //pakai ini untuk deafult Role User
        Set<Roles> roles = new HashSet<>();
        Roles role = roleRepository.findByRoleName(ERole.USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, " Role is not found"));
        roles.add(role);
        user.setRoles(roles);

//        Set<String> strRoles = signupRequest.getRole();
//        Set<Roles> roles = new HashSet<>();
//
//        try {
//            if (strRoles == null) {
//                Roles role = roleRepository.findByRoleName(ERole.USER)
//                        .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
//                roles.add(role);
//            } else {
//                strRoles.forEach(role -> {
//                    Roles roles1 = roleRepository.findByRoleName(ERole.valueOf(role))
//                            .orElseThrow(() -> new RuntimeException("Error: Role " + role + " is not found"));
//                    roles.add(roles1);
//                });
//            }
//
//            user.setRoles(roles);
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ada kesalahan pada Role");
//        }


        //kupindah bawah biar kalo kena validasi ga ngirim otp
        String otp = otpUtil.generateOtp();

        RegisterOtp registerOtp = new RegisterOtp();
        registerOtp.setOtp(otp);
        registerOtp.setOtpGenerateTime(LocalDateTime.now());
        user.addRegisterOtp(registerOtp);
        user.setIsActive(false);
        userRepository.save(user);
        registerOtpRepository.save(registerOtp);

        try {
            emailUtil.sendOtpEmail(signupRequest.getEmail(), otp);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send OTP please try again");
        }

        return WebResponse.<String>builder()
                .data("User registered successfully")
                .build();
    }

    @Override
    public WebResponse<String> registerAdmin(SignupRequest signupRequest) {
        Boolean usernameExist = userRepository.existsByUsername(signupRequest.getUsername());
        if(Boolean.TRUE.equals(usernameExist)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username telah ada");
        }

        Boolean emailExist = userRepository.existsByEmailAddress(signupRequest.getEmail());
        if(Boolean.TRUE.equals(emailExist)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email telah ada");
        }

        Boolean phoneExist = userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber());
        if(Boolean.TRUE.equals(phoneExist)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nomor telepon telah ada");
        }

        User user = User.builder().username(signupRequest.getUsername()).
                emailAddress(signupRequest.getEmail()).
                password(passwordEncoder.encode(signupRequest.getPassword())).
                phoneNumber(signupRequest.getPhoneNumber()).
        build();

        Set<Roles> roles = new HashSet<>();
        Roles role = roleRepository.findByRoleName(ERole.ADMIN)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, " Role is not found"));
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        return WebResponse.<String>builder()
                .data("Admin registered successfully")
                .build();
    }
}

