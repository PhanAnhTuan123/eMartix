package com.eMartix.authservice.service;

public interface EmailService {
    void sendEmailProviderToken(String to, String subject, String otp);

    void sendVerificationEmail(String to, String token);

    void sendPasswordResetEmail(String to, String token);

    void sendWelcomeEmail(String to);
}
