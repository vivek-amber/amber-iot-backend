package com.device.exception;

public class MqttSubscribeException extends RuntimeException {

    public MqttSubscribeException(String message) {
        super(message);
    }

    public MqttSubscribeException(String message, Throwable cause) {
        super(message,cause);
    }
}
