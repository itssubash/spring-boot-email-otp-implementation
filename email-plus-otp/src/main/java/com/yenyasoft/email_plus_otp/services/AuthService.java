package com.yenyasoft.email_plus_otp.services;

import com.yenyasoft.email_plus_otp.dtos.ApiResponse;
import com.yenyasoft.email_plus_otp.dtos.ForgotPasswordRequest;
import com.yenyasoft.email_plus_otp.dtos.LoginRequest;
import com.yenyasoft.email_plus_otp.dtos.RegisterRequest;
import com.yenyasoft.email_plus_otp.dtos.ResetPasswordRequest;
import com.yenyasoft.email_plus_otp.dtos.TokenResponse;
import com.yenyasoft.email_plus_otp.dtos.UserResponse;

/**
 * Author: Subash
 * Created: 8/25/2026
 * File: AuthService.java
 */
public interface AuthService {
    UserResponse createUser(RegisterRequest request);
    TokenResponse loginUser(LoginRequest request);
    ApiResponse forgotPassword(ForgotPasswordRequest request);
    ApiResponse resetPassword(ResetPasswordRequest request);

}
