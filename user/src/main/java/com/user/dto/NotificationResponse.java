package com.user.dto;

import java.time.LocalDateTime;

import com.user.enums.NotificationType;

public record NotificationResponse(
    String id,
    String title,
    String body,
    NotificationType type,
    Boolean read,
    LocalDateTime createdAt
) {

}
