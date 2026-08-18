package com.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSettingsEntity {
    
    @Id
    private String id;

    @Builder.Default
    private boolean deviceOffline = false;

    @Builder.Default
    private boolean deviceOnline = false;

    @Builder.Default
    private boolean firmwareUpdates = true;

    @Builder.Default
    private boolean automationExecuted = false;

    @Builder.Default
    private boolean automationFailed = false;

    @Builder.Default
    private boolean sceneTriggered = false;

    @Builder.Default
    private boolean securityAlerts = true;

    @Builder.Default
    private boolean suspiciousActivity = true;
}
