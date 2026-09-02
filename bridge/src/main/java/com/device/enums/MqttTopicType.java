package com.device.enums;

public enum MqttTopicType {

    ACK("ack"),
    STATUS("status");

    private final String suffix;

    MqttTopicType(String suffix) {
        this.suffix = suffix;
    }

    public static MqttTopicType fromTopic(String topic) {
        
        for (MqttTopicType type : values()) {

            if (topic.endsWith("/" + type.suffix)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown topic : " + topic);
    }
}
