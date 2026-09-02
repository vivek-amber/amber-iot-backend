package com.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.NotificationEntity;

public interface Notificationrepository extends JpaRepository<NotificationEntity, String> {
    
    List<NotificationEntity> findAllByUserIdOrderByCreatedAtDesc(String userId);

    long countByUserIdAndReadFalse(String userId);
}
