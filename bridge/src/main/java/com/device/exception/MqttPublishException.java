package com.device.exception;

public class MqttPublishException extends RuntimeException {

    public MqttPublishException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqttPublishException(String message) {
        super(message);
    }
}
