package com.yenyasoft.email_plus_otp.controller;

import com.yenyasoft.email_plus_otp.dtos.ApiResponse;
import com.yenyasoft.email_plus_otp.dtos.ForgotPasswordRequest;
import com.yenyasoft.email_plus_otp.dtos.LoginRequest;
import com.yenyasoft.email_plus_otp.dtos.RegisterRequest;
import com.yenyasoft.email_plus_otp.dtos.ResetPasswordRequest;
import com.yenyasoft.email_plus_otp.dtos.TokenResponse;
import com.yenyasoft.email_plus_otp.dtos.UserResponse;
import com.yenyasoft.email_plus_otp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Subash
 * Created: 8/25/2026
 * File: AuthController.java
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(authService.createUser(registerRequest),HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.loginUser(loginRequest),HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return new ResponseEntity<>(authService.forgotPassword(request), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        return new ResponseEntity<>(authService.resetPassword(request), HttpStatus.OK);
    }
}
