package com.finalproject.mooc.controller;

import com.finalproject.mooc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpRegisterController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Melakukan verifikasi akun dan aktivasi user yang diregistrasi")
    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(
            @RequestParam String email,
            @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @Operation(summary = "Melakukan regenerate untuk mengirimkan otp ulang")
    @PostMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }
}
