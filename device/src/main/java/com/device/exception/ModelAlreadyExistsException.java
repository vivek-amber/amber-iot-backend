package com.device.exception;

public class ModelAlreadyExistsException extends RuntimeException{
    
    public ModelAlreadyExistsException(String message) {
        super(message);
    }

    public ModelAlreadyExistsException(String message, Throwable cause) {
        super(message,cause);
    }
}
