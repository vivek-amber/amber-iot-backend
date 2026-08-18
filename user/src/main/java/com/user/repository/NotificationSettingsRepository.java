package com.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.NotificationSettingsEntity;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettingsEntity, String> {
    
}
