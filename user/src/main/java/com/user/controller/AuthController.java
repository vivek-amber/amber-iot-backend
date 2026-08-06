package com.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.dto.LoginRequest;
import com.user.dto.RegisterRequest;
import com.user.dto.ResendOtpRequest;
import com.user.dto.ResetPasswordRequest;
import com.user.dto.VerifyEmailRequest;
import com.user.helper.ApiResponse;
import com.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> registerUser(@Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
    }

    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<Map<String,Object>>> registerUser(@Valid @RequestBody VerifyEmailRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyEmail(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginUser(@Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok().body(authService.loginUser(request));
    }
    
    @PostMapping("/resend-verification-otp")
    public ResponseEntity<ApiResponse<Map<String, Object>>> resendVerificationOtp(@Valid @RequestBody ResendOtpRequest request) {

        return ResponseEntity.ok(authService.resendVerificationOtp(request));
    }

    @PostMapping("/reset-password-otp")
    public ResponseEntity<ApiResponse<Map<String, Object>>> resetPasswordOtp(@Valid @RequestBody ResendOtpRequest request) {

        return ResponseEntity.ok(authService.resetPasswordOtp(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Map<String, Object>>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

        return ResponseEntity.ok(authService.resetPassword(request));
    }
    
}
