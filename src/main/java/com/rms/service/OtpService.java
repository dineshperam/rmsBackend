package com.rms.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
    private final Map<String, String> otpStorage = new HashMap<>();
    private static final Random random = new SecureRandom();

    /**
     * Generates and stores a 6-digit OTP for the given username.
     */
    public synchronized String generateOtp(String username) {
        String otp = String.format("%06d", random.nextInt(999999));
        otpStorage.put(username, otp);
        System.out.println("🔹 Generated OTP for " + username + ": " + otp);
        return otp;
    }

    /**
     * Validates the OTP for the given username.
     */
    public synchronized boolean validateOtp(String username, String otp) {
        String storedOtp = otpStorage.get(username);

        // Debugging logs
        System.out.println("🔹 Validating OTP for " + username);
        System.out.println("🔹 Stored OTP: " + storedOtp);
        System.out.println("🔹 Provided OTP: " + otp);

        // Ensure storedOtp is not null and matches the provided OTP
        if (storedOtp != null && storedOtp.equals(otp)) {
            System.out.println("✅ OTP is valid for " + username);
            return true;
        } else {
            System.out.println("❌ Invalid OTP for " + username);
            return false;
        }
    }

    /**
     * Clears the stored OTP for the given username.
     */
    public synchronized void clearOtp(String username) {
        if (otpStorage.containsKey(username)) {
            System.out.println("🔴 Clearing OTP for " + username);
            otpStorage.remove(username);
        }
    }
}
