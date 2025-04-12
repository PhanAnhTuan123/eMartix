package com.eMartix.authservice.service.impl;

import com.eMartix.authservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailProviderToken(String to, String subject, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("example@yourdomain.com"); // Không quan trọng với Mailtrap
        message.setTo(to);
        message.setSubject(subject);
        String body = "Welcome to eMartix -> the code for your email verification is: " + otp;
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendVerificationEmail(String to, String token) {

    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {

    }

    @Override
    public void sendWelcomeEmail(String to) {

    }
}
