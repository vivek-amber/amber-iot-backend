package com.automation.exception;

public class SceneAlreadyExistsException extends RuntimeException {
    
    public SceneAlreadyExistsException(String message) {
        super(message);
    }

    public SceneAlreadyExistsException(String message, Throwable cause) {
        super(message,cause);
    }
}
