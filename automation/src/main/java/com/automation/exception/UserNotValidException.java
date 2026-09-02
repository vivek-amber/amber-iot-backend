package com.automation.exception;

public class UserNotValidException extends RuntimeException {
    
    public UserNotValidException(String message) {
        super(message);
    }

    public UserNotValidException(String message, Throwable cause) {
        super(message,cause);
    }
}
