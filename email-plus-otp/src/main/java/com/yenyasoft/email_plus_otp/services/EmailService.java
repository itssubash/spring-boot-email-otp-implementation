package com.yenyasoft.email_plus_otp.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendOtpEmail(String to, String otp) {
        logger.info("Generated OTP for {}: {}", to, otp);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp + "\n\nThis OTP is valid for 5 minutes.");
            mailSender.send(message);
            logger.info("OTP email sent to {}", to);
        } catch (Exception e) {
            logger.warn("Could not send email to {} ({}). OTP for dev/testing: {}", to, e.getMessage(), otp);
        }
    }
}
