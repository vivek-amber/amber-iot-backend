package com.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sucurity.model.SecurityPrincipal;
import com.user.dto.NotificationResponse;
import com.user.dto.NotificationSettingsRequestDto;
import com.user.helper.ApiResponse;
import com.user.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    
    @PutMapping
    public ResponseEntity<ApiResponse<Map<Object, Object>>> updateNotifications(
        @AuthenticationPrincipal SecurityPrincipal principal,
            @RequestBody NotificationSettingsRequestDto request) 
    {

        return ResponseEntity.ok(notificationService.updateNotifications(principal.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            @RequestParam(defaultValue = "1") Integer page) {

        return ResponseEntity.ok(notificationService.getNotifications());
    }
    
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {

        return ResponseEntity.ok(notificationService.getUnreadCount());
    }
}
