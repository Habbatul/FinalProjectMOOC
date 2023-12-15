package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.requests.OtpRequest;
import com.finalproject.mooc.model.requests.RegenerateOtpReq;
import com.finalproject.mooc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OtpRegisterController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Melakukan verifikasi akun dan aktivasi user yang diregistrasi")
    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestBody OtpRequest req) {
        return new ResponseEntity<>(userService.verifyAccount(req.getEmail(), req.getOtp()), HttpStatus.OK);
    }

    @Operation(summary = "Melakukan regenerate untuk mengirimkan otp ulang")
    @PostMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestBody RegenerateOtpReq req) {
        return new ResponseEntity<>(userService.regenerateOtp(req.getEmail()), HttpStatus.OK);
    }
}
