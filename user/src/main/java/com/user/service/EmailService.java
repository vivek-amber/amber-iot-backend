package com.user.service;


import java.time.Instant;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.user.repository.EmailVerificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final EmailVerificationRepository emailVerificationRepository;
     

    @Async 
    public void sendOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Email Verification Code");

        message.setText(
                "Dear User,\n\n" +
                        "Welcome to Amber.\n\n" +
                        "To complete your account registration, please verify your email address using the One-Time Password (OTP) below:\n\n"
                        +
                        "Verification Code: " + otp + "\n\n" +
                        "This OTP is valid for 10 minutes and can only be used once.\n\n" +
                        "If you did not request this verification, you can safely ignore this email.\n\n" +
                        "Thank you,\n" +
                        "Amber Support Team");

        try {
            mailSender.send(message);

        } catch (Exception ex) {
            log.error("Error while sending email", ex);
        }
    }
    
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredOtps() {
        emailVerificationRepository.deleteByExpiresAtBefore(Instant.now());
    }
}