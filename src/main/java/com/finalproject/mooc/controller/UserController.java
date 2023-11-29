package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.requests.UpdateUserPassword;
import com.finalproject.mooc.model.requests.UpdateUserRequest;
import com.finalproject.mooc.model.responses.UserResponse;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Menampilkan data user yang sedang login")
    @GetMapping("/user")
    public ResponseEntity<WebResponse<UserResponse>> resetPassword(@RequestHeader String username) {
        return ResponseEntity.ok(WebResponse.<UserResponse>builder().data(userService.showUserByUsername(username)).build());
    }

//gunakan BindingResult dibawah modelAttribute agar File bisa null
    @Operation(summary = "(Jenis : Multipart-Form-Data) Mengedit profile beserta foto profil, tidak akan diupdate untuk field yang kosong")
    @PutMapping(value = "user/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<WebResponse<String>> updateProfile(
            @RequestHeader String username,
            @ModelAttribute UpdateUserRequest userRequest,
            BindingResult result) {
        try {
            userService.updateProfile(username, userRequest);
            return ResponseEntity.ok(WebResponse.<String>builder().data("Profile berhasil diUpdate").build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    WebResponse.<String>builder().error("Failed to update profile").build());
        }
    }
    @Operation(summary = "Mengupdate password user untuk user yang sedang login")
    @PutMapping(value = "user/password",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> updatePassword(
            @RequestHeader String username,
            @RequestBody UpdateUserPassword passwordReq
            ){
        userService.updatePassword(username, passwordReq);
        return  ResponseEntity.ok(WebResponse.<String>builder().data("Sukses mengubah password").build());
    }

}
