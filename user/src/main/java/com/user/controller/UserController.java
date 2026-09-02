package com.user.controller;

import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sucurity.model.SecurityPrincipal;
import com.user.dto.ResponseUser;
import com.user.dto.ChangePasswordDto;
import com.user.dto.SessionResponseDto;
import com.user.helper.ApiResponse;
import com.user.service.AuthService;
import com.user.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;

    private final AuthService authService;


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ResponseUser>> fetchUserDetails(
            @AuthenticationPrincipal SecurityPrincipal principal) {

        String uid = principal.getUsername();
        return ResponseEntity.ok(userService.fetchUserDetails(uid));
    }
    
    @PostMapping("/changePassword")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> changePassword(
            @Valid @RequestBody ChangePasswordDto request,
            @AuthenticationPrincipal SecurityPrincipal principal      
    ) 
    {

        return ResponseEntity
                .ok(authService.changePassword(principal.getUsername(), principal.getCompanyId(), request));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> logout() 
    {

        return ResponseEntity.ok(authService.logout());
    }
    
    @GetMapping("/session")
    public ResponseEntity<ApiResponse<List<SessionResponseDto>>> getAllSessions() {

        return ResponseEntity.ok(
                userService.getAllSessions());
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> deleteSession(@PathVariable String sessionId) {

        return ResponseEntity.ok(authService.deleteSession(sessionId));
    }

    @DeleteMapping("/session")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> deleteAllSession() {

        return ResponseEntity.ok(authService.deleteAllSessions());
    }

    @DeleteMapping("/account")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> deleteAccount() {

        return ResponseEntity.ok(authService.deleteAccount());
    }
}
 