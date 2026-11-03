package com.user.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import java.lang.RuntimeException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.dto.ChangePasswordDto;
import com.user.dto.LoginRequest;
import com.user.dto.RefreshTokenRequest;
import com.user.dto.RegisterRequest;
import com.user.dto.ResendOtpRequest;
import com.user.dto.ResetPasswordRequest;
import com.user.dto.VerifyEmailRequest;
import com.user.dto.WebLoginRequest;
import com.user.entity.UserEntity;
import com.user.entity.UserSessionEntity;
import com.user.entity.VerificationOtpEntity;
import com.user.exception.UserAlreadyExistException;
import com.user.exception.UserNotExistException;
import com.user.exception.WrongUsernamePasswordException;
import com.user.helper.ApiResponse;
import com.user.helper.HashUtil;
import com.user.mapper.UserMapper;
import com.user.repository.EmailVerificationRepository;
import com.user.repository.SessionRepository;
import com.user.repository.UserRepository;
import com.sucurity.constants.SecurityConstants;
import com.sucurity.context.SecurityContextUtil;
import com.sucurity.exception.InvalidTokenException;
import com.sucurity.jwt.JwtUtil;
import com.sucurity.model.AuthUser;
import com.user.exception.EmailAlreadyVerifiedException;
import com.user.exception.EmailNotVerifiedException;
import com.user.exception.InvalidOtpException;
import com.user.exception.OtpExpiredException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    private final SessionRepository sessionRepository;

    private final EmailService emailService;

    private final NotificationService notificationService;

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

            UserSessionEntity session = sessionRepository
                            .findByUserIdAndDeviceId(
                                            user.getId(),
                                            request.deviceId())
                            .orElseGet(() -> UserSessionEntity.builder()
                                            .id(UUID.randomUUID().toString())
                                            .userId(user.getId())
                                            .deviceId(request.deviceId())
                                            .active(true)
                                            .build());

            session.setActive(true);

            if (request.deviceName() != null && !request.deviceName().isBlank()) {
                    session.setDeviceName(request.deviceName());
            }

            String refreshToken = jwtUtil.generateRefreshToken(
                            user.getId(),
                            session.getId());

            String refreshTokenFingerprint = HashUtil.sha256(refreshToken);
            session.setRefreshTokenHash(
                            passwordEncoder.encode(refreshTokenFingerprint));

            sessionRepository.save(session);

            AuthUser authUser = new AuthUser(
                            user.getId(),
                            user.getCompanyId(),
                            user.getRoles(),
                            session.getId(),
                            user.getTokenVersion());

            String accessToken = jwtUtil.generateAccessToken(
                            authUser,
                            session.getId(),
                            user.getTokenVersion());

            Map<String, Object> data = Map.of(
                            "accessToken", accessToken,
                            "refreshToken", refreshToken);

            return ApiResponse.success("Login successful", data);

    }

    public ApiResponse<Map<Object, Object>> loginWebUser(WebLoginRequest request, HttpServletResponse response) {

            UserEntity user = userRepository.findByEmail(request.email())
                            .orElseThrow(() -> new UserNotExistException("User with this email do not exists."));

            boolean isPasswordCorrect = passwordEncoder.matches(request.password(), user.getPassword());

            if (!isPasswordCorrect) {

                    throw new WrongUsernamePasswordException("Wrong password");
            }

            String refreshToken = jwtUtil.generateRefreshToken(
                            user.getId(),
                            null);

            AuthUser authUser = new AuthUser(
                            user.getId(),
                            user.getCompanyId(),
                            user.getRoles(),
                            null,
                            user.getTokenVersion());

            String accessToken = jwtUtil.generateAccessToken(
                            authUser,
                            null,
                            user.getTokenVersion());

            ResponseCookie accessCookie = ResponseCookie
                            .from(SecurityConstants.ACCESS_TOKEN, accessToken)
                            .httpOnly(true)
                            .secure(true) // false locally as using http
                            .sameSite("None")
                            .path("/")
                            .maxAge(Duration.ofMinutes(15))
                            .build();
            ResponseCookie refreshCookie = ResponseCookie
                            .from(SecurityConstants.REFRESH_TOKEN, refreshToken)
                            .httpOnly(true)
                            .secure(true) // false locally as using http
                            .sameSite("None")
                            .path("/")
                            .maxAge(Duration.ofDays(30))
                            .build();

            response.addHeader(
                            HttpHeaders.SET_COOKIE,
                            accessCookie.toString());
            response.addHeader(
                            HttpHeaders.SET_COOKIE,
                            refreshCookie.toString());
            return ApiResponse.success("Login successful", Collections.emptyMap());

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

        // register notification one user verified
        notificationService.registerNotification(user.getId());

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

    public ApiResponse<Map<Object, Object>> changePassword(String uid, String companyId, ChangePasswordDto request) {

            // Check if user exists
            UserEntity user = userRepository
                            .findByIdAndCompanyId(uid, companyId)
                            .orElseThrow(() -> new UserNotExistException("User not found"));

            if (!passwordEncoder.matches(
                            request.currentPassword(),
                            user.getPassword())) {
                    throw new WrongUsernamePasswordException(
                                    "Current password is incorrect");
            }

            user.setPassword(
                            passwordEncoder.encode(request.newPassword()));

            userRepository.save(user);

            return ApiResponse.success("Password updated.", Collections.emptyMap());
    }

    public ApiResponse<Map<String, Object>> refreshToken(RefreshTokenRequest request) {

            String refreshToken = request.refreshToken();

            jwtUtil.validateToken(refreshToken);

            String sessionId = jwtUtil.getSessionId(refreshToken);

            UserSessionEntity session = sessionRepository.findById(sessionId)
                            .orElseThrow(() -> new InvalidTokenException(
                                            "Invalid session"));

            if (!Boolean.TRUE.equals(session.getActive())) {
                    throw new InvalidTokenException(
                                    "Session has been revoked");
            }

            boolean isValid = passwordEncoder.matches(
                            HashUtil.sha256(refreshToken),
                            session.getRefreshTokenHash());

            if (!isValid) {
                    throw new InvalidTokenException(
                                    "Invalid refresh token");
            }

            UserEntity user = userRepository.findById(
                            session.getUserId())
                            .orElseThrow(() -> new UserNotExistException(
                                            "User not found"));

            AuthUser authUser = new AuthUser(
                            user.getId(),
                            user.getCompanyId(),
                            user.getRoles(),
                            session.getId(),
                            user.getTokenVersion());

            String newRefreshToken = jwtUtil.generateRefreshToken(
                            user.getId(),
                            session.getId());

            String refreshTokenFingerprint = HashUtil.sha256(newRefreshToken);

            session.setRefreshTokenHash(
                            passwordEncoder.encode(
                                            refreshTokenFingerprint));

            sessionRepository.save(session);

            String newAccessToken = jwtUtil.generateAccessToken(
                            authUser,
                            session.getId(),
                            user.getTokenVersion());

            Map<String, Object> data = Map.of(
                            "accessToken", newAccessToken,
                            "refreshToken", newRefreshToken);
            return ApiResponse.success(
                            "Token refreshed successfully",
                            data);
    }

    public ApiResponse<Map<String, Object>> refreshWebToken(HttpServletRequest request, HttpServletResponse response) {

            String refreshToken = null;

            if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {

                            if (SecurityConstants.REFRESH_TOKEN
                                            .equals(cookie.getName())) {

                                    refreshToken = cookie.getValue();
                                    break;
                            }
                    }
            }

            if (refreshToken == null) {
                    throw new InvalidTokenException(
                                    "Refresh token missing");
            }
            
            jwtUtil.validateToken(refreshToken);

            String userId = jwtUtil.getUserId(refreshToken);
            
            UserEntity user = userRepository.findById(
                            userId)
                            .orElseThrow(() -> new UserNotExistException(
                                            "User not found"));

            AuthUser authUser = new AuthUser(
                            user.getId(),
                            user.getCompanyId(),
                            user.getRoles(),
                            null,
                            user.getTokenVersion());

            String newRefreshToken = jwtUtil.generateRefreshToken(
                            user.getId(),
                            null);


            String newAccessToken = jwtUtil.generateAccessToken(
                                authUser,
                            null,
                            user.getTokenVersion());

                            ResponseCookie accessCookie = ResponseCookie
                            .from(SecurityConstants.ACCESS_TOKEN, newAccessToken)
                            .httpOnly(true)
                            .secure(true) // false locally as using http
                            .sameSite("None")
                            .path("/")
                            .maxAge(Duration.ofMinutes(15))
                            .build();
            ResponseCookie refreshCookie = ResponseCookie
                            .from(SecurityConstants.REFRESH_TOKEN, newRefreshToken)
                            .httpOnly(true)
                            .secure(true) // false locally as using http
                            .sameSite("None")
                            .path("/")
                            .maxAge(Duration.ofDays(30))
                            .build();

            response.addHeader(
                            HttpHeaders.SET_COOKIE,
                            accessCookie.toString());
            response.addHeader(
                            HttpHeaders.SET_COOKIE,
                            refreshCookie.toString());
            return ApiResponse.success("Token refreshed successfully", Collections.emptyMap());
    }

    public ApiResponse<Map<Object, Object>> logout() {

            String sessionId = SecurityContextUtil.currentSessionId();

            sessionRepository.deleteById(sessionId);

            return ApiResponse.success(
                            "Logged out successfully",
                            Collections.emptyMap());

    }

    public ApiResponse<Map<Object, Object>> deleteSession(String sessionId) {

            // Check session exists
            UserSessionEntity session = sessionRepository.findById(sessionId)
                            .orElseThrow(() -> new RuntimeException("Session not found"));

            sessionRepository.delete(session);

            return ApiResponse.success("Session logged out successfully.", Collections.emptyMap());
    }

    public ApiResponse<Map<Object, Object>> deleteAllSessions() {

            // Check session exists
            String userId = SecurityContextUtil.currentUserId();
            List<UserSessionEntity> sessions = sessionRepository.findByUserId(userId);

            sessionRepository.deleteAll(sessions);
            return ApiResponse.success("Session logged out successfully.", Collections.emptyMap());
    }

    @Transactional
    public ApiResponse<Map<Object, Object>> deleteAccount() {

            String userId = SecurityContextUtil.currentUserId();

            UserEntity user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

            sessionRepository.deleteAllByUserId(userId);

            notificationService.deleteNotificationSettings(userId);

            userRepository.delete(user);

            return ApiResponse.success(
                            "Account deleted successfully.",
                            Collections.emptyMap());

    }

}
