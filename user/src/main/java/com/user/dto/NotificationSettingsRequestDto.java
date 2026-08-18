package com.user.dto;


public record NotificationSettingsRequestDto(

    Boolean deviceOffline,
    Boolean deviceOnline,
    Boolean firmwareUpdates,

    Boolean automationExecuted,
    Boolean automationFailed,
    Boolean sceneTriggered,

    Boolean securityAlerts,
    Boolean suspiciousActivity
) {

}