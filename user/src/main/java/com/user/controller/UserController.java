package com.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sucurity.model.SecurityPrincipal;
import com.user.dto.ResponseUser;
import com.user.helper.ApiResponse;
import com.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ResponseUser>> fetchUserDetails(@AuthenticationPrincipal SecurityPrincipal principal) {
        
        String uid = principal.getUsername();
        return ResponseEntity.ok(userService.fetchUserDetails(uid));
    }
}
 