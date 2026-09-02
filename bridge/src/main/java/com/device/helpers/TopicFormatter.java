package com.device.helpers;


import com.device.constants.MqttTopics;

public final class TopicFormatter {

    private TopicFormatter() {
    }

    public static String commandTopic(
            String companyId,
            String deviceId) {

        return String.format(
                MqttTopics.DEVICE_COMMAND,
                companyId,
                deviceId);
    }
    
    public static String extractDeviceId(String topic) {

        String[] parts = topic.split("/");

        return parts[3];
    }

    public static String extractCompanyId(String topic) {

        String[] parts = topic.split("/");

        return parts[1];
    }
}