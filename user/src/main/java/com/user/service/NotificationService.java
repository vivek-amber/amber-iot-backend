package com.user.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sucurity.context.SecurityContextUtil;
import com.user.dto.NotificationResponse;
import com.user.dto.NotificationSettingsRequestDto;
import com.user.entity.NotificationEntity;
import com.user.entity.NotificationSettingsEntity;
import com.user.helper.ApiResponse;
import com.user.mapper.NotificationMapper;
import com.user.repository.NotificationSettingsRepository;
import com.user.repository.Notificationrepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationSettingsRepository notificationSettingsRepository;

    private final Notificationrepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public void registerNotification(String userId) {
        
        // Find if already notification reg for this user
        if (notificationSettingsRepository.existsById(userId)) {
            return;
        }

        NotificationSettingsEntity notification = NotificationSettingsEntity.builder().id(userId).build();

        notificationSettingsRepository.save(notification);

    }

    public ApiResponse<Map<Object, Object>> updateNotifications(
        String userId,
            NotificationSettingsRequestDto request) {

        NotificationSettingsEntity settings = notificationSettingsRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Settings not found"));

        if (request.deviceOffline() != null) {
            settings.setDeviceOffline(request.deviceOffline());
        }

        if (request.deviceOnline() != null) {
            settings.setDeviceOnline(request.deviceOnline());
        }

        if (request.firmwareUpdates() != null) {
            settings.setFirmwareUpdates(request.firmwareUpdates());
        }

        if (request.automationExecuted() != null) {
            settings.setAutomationExecuted(request.automationExecuted());
        }

        if (request.automationFailed() != null) {
            settings.setAutomationFailed(request.automationFailed());
        }

        if (request.sceneTriggered() != null) {
            settings.setSceneTriggered(request.sceneTriggered());
        }

        if (request.securityAlerts() != null) {
            settings.setSecurityAlerts(request.securityAlerts());
        }

        if (request.suspiciousActivity() != null) {
            settings.setSuspiciousActivity(request.suspiciousActivity());
        }

        notificationSettingsRepository.save(settings);

        return ApiResponse.success("Details fetched successfully.", Collections.emptyMap());
    }

    // Called By delete user
    public void deleteNotificationSettings(String userId) {

        notificationSettingsRepository.deleteById(userId);
    }

    // Currently marking read after fetch later have  a seperate endpoint
    public ApiResponse<List<NotificationResponse>> getNotifications() {

        String userId = SecurityContextUtil.currentUserId();

        List<NotificationEntity> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        List<NotificationResponse> res = notifications
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
        
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);

        return ApiResponse.success("Notifications fetched.", res);
    }

    public ApiResponse<Long> getUnreadCount() {
        
        String userId = SecurityContextUtil.currentUserId();

        long count = notificationRepository.countByUserIdAndReadFalse(userId);

        return ApiResponse.success("Unread Count fetched.", count);
    }
}
