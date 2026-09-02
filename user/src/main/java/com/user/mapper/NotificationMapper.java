package com.user.mapper;

import org.springframework.stereotype.Component;

import com.user.dto.NotificationResponse;
import com.user.entity.NotificationEntity;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(
            NotificationEntity notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getBody(),
                notification.getType(),
                notification.getRead(),
                notification.getCreatedAt());
    }
}
