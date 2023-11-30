package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.requests.auth.LoginRequest;
import com.finalproject.mooc.model.requests.auth.SignupRequest;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.model.responses.auth.JwtResponse;
import com.finalproject.mooc.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Melakukan authentikasi user untuk mendapat hak akses (login)")
    @PermitAll
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest login, HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.authenticateUser(login, response));
    }

    @Operation(summary = "Menambahkan user (register)")
    @PostMapping("/signup")
    public ResponseEntity<WebResponse<String>> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok().body(authService.registerUser(signupRequest));
    }

    @Operation(summary = "Menambahkan Admin/Teacher, Ini untuk tester hak akses admin")
    @PostMapping("/signup/admin")
    public ResponseEntity<WebResponse<String>> registerAdmin(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok().body(authService.registerAdmin(signupRequest));
    }
}
