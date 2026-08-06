package com.device.exception;

public class ModelConfigNotFoundException extends RuntimeException {

    public ModelConfigNotFoundException(String message) {
        super(message);
    }

    public ModelConfigNotFoundException(String message, Throwable cause) {
        super(message,cause);
    }
}