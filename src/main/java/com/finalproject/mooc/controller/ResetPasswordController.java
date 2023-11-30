package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.requests.ResetPasswordRequest;
import com.finalproject.mooc.model.responses.ResetTokenResponse;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResetPasswordController {

    @Autowired
    private UserService userService;
    @Operation(summary = "Melakukan generate token untuk izin lupa password")
    @GetMapping("/forget-password/generate-token")
    public ResponseEntity<ResetTokenResponse> generateToken(@RequestHeader String emailAddress) {
        return ResponseEntity.ok(ResetTokenResponse.builder().resetToken(userService.makeTokenResetPassword(emailAddress)).build());
    }

    @Operation(summary = "Melakukan ubah password (lupa password), durasi izin ubah password dihitung setelah generate token")
    @PutMapping("/forget-password/reset")
    public ResponseEntity<WebResponse<String>> resetPassword(@RequestBody ResetPasswordRequest passwordRequest) {
        userService.resetPassword(passwordRequest);
        return ResponseEntity.ok(WebResponse.<String>builder().data("Password reset successfully").build());
    }

}
