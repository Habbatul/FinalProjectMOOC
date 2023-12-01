package com.finalproject.mooc.service;

import com.finalproject.mooc.model.requests.ResetPasswordRequest;
import com.finalproject.mooc.model.requests.UpdateUserPassword;
import com.finalproject.mooc.model.requests.UpdateUserRequest;
import com.finalproject.mooc.model.responses.UserResponse;

import java.io.IOException;

public interface UserService {
    void updateProfile(String username, UpdateUserRequest userRequest) throws IOException;
    void updatePassword(String username, UpdateUserPassword passwordRequest);
    void resetPassword(ResetPasswordRequest passwordRequest);
    String makeTokenResetPassword(String emailAddress);

    UserResponse showUserByUsername(String username);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

}
