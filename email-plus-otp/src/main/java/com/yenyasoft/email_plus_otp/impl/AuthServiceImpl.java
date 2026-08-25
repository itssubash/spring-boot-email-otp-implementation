package com.yenyasoft.email_plus_otp.impl;

import com.yenyasoft.email_plus_otp.dtos.*;
import com.yenyasoft.email_plus_otp.exceptions.AlreadyExistsException;
import com.yenyasoft.email_plus_otp.exceptions.ResourceNotFoundException;
import com.yenyasoft.email_plus_otp.models.User;
import com.yenyasoft.email_plus_otp.repositories.UserRepository;
import com.yenyasoft.email_plus_otp.security.JwtService;
import com.yenyasoft.email_plus_otp.services.AuthService;
import com.yenyasoft.email_plus_otp.services.EmailService;
import com.yenyasoft.email_plus_otp.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 8/25/2026
 * File: AuthServiceImpl.java
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final EmailService emailService;

    @Override
    public UserResponse createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("The requested email is already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("The requested username is already in use");
        }

        User registeredUser =  User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(Boolean.TRUE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(registeredUser);
        return UserResponse.toResponse(registeredUser);
    }

    @Override
    public TokenResponse loginUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenPair tokens = new TokenPair(jwtService.generateAccessToken(authentication), jwtService.generateRefreshToken(authentication) );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse userResponse = UserResponse.toResponse(user);

        return TokenResponse.builder()
                .tokenPair(tokens)
                .user(userResponse)
                .build();
    }

    @Override
    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email"));
        String otp = otpService.generateOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), otp);
        return new ApiResponse("OTP sent to your email. It is valid for 5 minutes.", true);
    }

    @Override
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email"));
        if (!otpService.verifyOtp(user.getEmail(), request.getOtp())) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        otpService.clearOtp(user.getEmail());
        return new ApiResponse("Password has been reset successfully", true);
    }
}
