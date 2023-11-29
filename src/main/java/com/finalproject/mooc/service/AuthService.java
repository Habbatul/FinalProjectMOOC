package com.finalproject.mooc.service;

import com.finalproject.mooc.model.requests.auth.LoginRequest;
import com.finalproject.mooc.model.requests.auth.SignupRequest;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.model.responses.auth.JwtResponse;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest login, HttpServletResponse response);
    WebResponse<String> registerUser(SignupRequest signupRequest);
}
