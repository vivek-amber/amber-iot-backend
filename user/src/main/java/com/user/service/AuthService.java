package com.user.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.dto.LoginRequest;
import com.user.dto.RegisterRequest;
import com.user.dto.ResendOtpRequest;
import com.user.dto.ResetPasswordRequest;
import com.user.dto.VerifyEmailRequest;
import com.user.entity.UserEntity;
import com.user.entity.VerificationOtpEntity;
import com.user.exception.UserAlreadyExistException;
import com.user.exception.UserNotExistException;
import com.user.exception.WrongUsernamePasswordException;
import com.user.helper.ApiResponse;
import com.user.mapper.UserMapper;
import com.user.repository.EmailVerificationRepository;
import com.user.repository.UserRepository;

import com.sucurity.jwt.JwtUtil;
import com.sucurity.model.AuthUser;
import com.user.exception.EmailAlreadyVerifiedException;
import com.user.exception.EmailNotVerifiedException;
import com.user.exception.InvalidOtpException;
import com.user.exception.OtpExpiredException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    private final EmailService emailService;
    
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final JwtUtil jwtUtil;
    

    @Transactional
    public ApiResponse<Map<Object, Object>> registerUser(RegisterRequest request) {

        boolean userExists = userRepository.existsByEmailAndCompanyId(request.email(), request.companyId());

        if (userExists) {

            throw new UserAlreadyExistException("User with this email already exists.");
        }

        String encryptedPass = passwordEncoder.encode(request.password());

        UserEntity user = userMapper.mapRegisterRequestToEntity(request, encryptedPass);
        userRepository.save(user);

        String otp =
        String.valueOf(
            ThreadLocalRandom.current()
                .nextInt(100000,999999)
        );

        VerificationOtpEntity verification = new VerificationOtpEntity();

        verification.setEmail(request.email());
        verification.setOtp(otp);

        verification.setExpiresAt(
            Instant.now().plus(10, ChronoUnit.MINUTES)
        );

        emailVerificationRepository.save(verification);

        emailService.sendOtp(request.email(), otp);


        return ApiResponse.success("Account created successfully.", Collections.emptyMap());
    }
    
    public ApiResponse<Map<String, Object>> loginUser(LoginRequest request) {

        UserEntity user = userRepository.findByEmailAndCompanyId(request.email(), request.companyId())
                .orElseThrow(() -> new UserNotExistException("User with this email do not exists."));

        
        if (!user.getEmailVerified()) {
            throw new EmailNotVerifiedException("Email for this account not verified.");
        }
        
        boolean isPasswordCorrect = passwordEncoder.matches(request.password(), user.getPassword());

        if (!isPasswordCorrect) {

            throw new WrongUsernamePasswordException("Wrong password");
        }

        AuthUser authUser = new AuthUser(user.getId(), user.getCompanyId(), user.getRoles());
        String token = jwtUtil.generateToken(authUser);

        Map<String, Object> data = Map.of("token", token);

        return ApiResponse.success("Login successful", data);

    }

    @Transactional
    public ApiResponse<Map<String, Object>> verifyEmail(VerifyEmailRequest request) {

        VerificationOtpEntity verification = emailVerificationRepository
                .findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(
                        request.email())
                .orElseThrow(() -> new InvalidOtpException(
                        "Verification request not found"));

        if (Boolean.TRUE.equals(verification.getVerified())) {
            throw new EmailAlreadyVerifiedException(
                    "Email already verified");
        }

        if (!verification.getOtp().equals(request.otp())) {
            throw new InvalidOtpException("Invalid OTP");
        }

        if (verification.getExpiresAt().isBefore(Instant.now())) {
            throw new OtpExpiredException("OTP expired");
        }

        UserEntity user = userRepository
                .findByEmailAndCompanyId(request.email(), request.companyId())
                .orElseThrow(() -> new UserNotExistException(
                        "User not found"));

        user.setEmailVerified(true);
        userRepository.save(user);

        emailVerificationRepository.deleteByEmail(request.email());

        return ApiResponse.success("Account verified successfully.", Collections.emptyMap());
    }

    @Transactional
    public ApiResponse<Map<String, Object>> resendVerificationOtp(ResendOtpRequest request) {

        UserEntity user = userRepository.findByEmailAndCompanyId(request.email(), request.companyId())
                .orElseThrow(() -> new UserNotExistException("User not found"));

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new EmailAlreadyVerifiedException(
                    "Email already verified");
        }

        String otp = String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100000, 999999));

        VerificationOtpEntity verification = new VerificationOtpEntity();

        verification.setEmail(request.email());
        verification.setOtp(otp);
        verification.setVerified(false);
        verification.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));

        emailVerificationRepository.save(verification);

        emailService.sendOtp(request.email(), otp);

        return ApiResponse.success("OTP sent successfully", Collections.emptyMap());
    }

    @Transactional
    public ApiResponse<Map<String, Object>> resetPasswordOtp(ResendOtpRequest request) {

        userRepository.findByEmailAndCompanyId(request.email(), request.companyId())
                .orElseThrow(() -> new UserNotExistException("User not found"));

        String otp = String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100000, 999999));

        VerificationOtpEntity verification = new VerificationOtpEntity();

        verification.setEmail(request.email());
        verification.setOtp(otp);
        verification.setVerified(false);
        verification.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));

        emailVerificationRepository.save(verification);

        emailService.sendOtp(request.email(), otp);

        return ApiResponse.success("OTP sent successfully", Collections.emptyMap());
    }
    
    @Transactional
    public ApiResponse<Map<String, Object>> resetPassword(ResetPasswordRequest request) {

        VerificationOtpEntity verification = emailVerificationRepository
                .findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(request.email())
                .orElseThrow(() -> new InvalidOtpException("Verification request not found"));

        if (!verification.getOtp().equals(request.otp())) {
            throw new InvalidOtpException("Invalid OTP");
        }

        if (verification.getExpiresAt().isBefore(Instant.now())) {
            throw new OtpExpiredException("OTP expired");
        }

        UserEntity user = userRepository.findByEmailAndCompanyId(
                request.email(),
                request.companyId())
                .orElseThrow(() -> new UserNotExistException("User not found"));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        emailVerificationRepository.deleteByEmail(request.email());

        return ApiResponse.success("Password reset successfully", Collections.emptyMap());
    }

}
