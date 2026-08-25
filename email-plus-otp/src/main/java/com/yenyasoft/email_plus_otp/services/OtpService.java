package com.yenyasoft.email_plus_otp.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final long OTP_VALIDITY_MINUTES = 5;
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        otpStore.put(email.toLowerCase(), new OtpEntry(otp, LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES)));
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        if (email == null || otp == null) return false;
        OtpEntry entry = otpStore.get(email.toLowerCase());
        if (entry == null) return false;
        if (entry.expiresAt().isBefore(LocalDateTime.now())) {
            otpStore.remove(email.toLowerCase());
            return false;
        }
        return entry.otp().equals(otp.trim());
    }

    public void clearOtp(String email) {
        otpStore.remove(email.toLowerCase());
    }

    private record OtpEntry(String otp, LocalDateTime expiresAt) {
    }
}
