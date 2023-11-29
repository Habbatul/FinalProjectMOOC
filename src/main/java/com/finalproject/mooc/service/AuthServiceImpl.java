package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.entity.security.Roles;
import com.finalproject.mooc.enums.ERole;
import com.finalproject.mooc.model.requests.auth.LoginRequest;
import com.finalproject.mooc.model.requests.auth.SignupRequest;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.model.responses.auth.JwtResponse;
import com.finalproject.mooc.model.security.UserDetailsImpl;
import com.finalproject.mooc.repository.RoleRepository;
import com.finalproject.mooc.repository.UserRepository;
import com.finalproject.mooc.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private JwtUtil jwtUtils;

    @Value("${jwt.expiration.ms}")
    private int jwtExpirationMs;

    @Transactional(readOnly = true)
    @Override
    public JwtResponse authenticateUser(LoginRequest login, HttpServletResponse response) {
        //sementara gini dulu
        User user = userRepository.findUserByEmailAddress(login.getEmailAddress()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "User dengan email tidak ditemukan"));

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

        userRepository.save(user);

        return WebResponse.<String>builder()
                .data("User registered successfully")
                .build();
    }
}

