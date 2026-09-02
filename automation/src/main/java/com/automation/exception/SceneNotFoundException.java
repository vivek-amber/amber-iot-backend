package com.automation.exception;

public class SceneNotFoundException extends RuntimeException{

    public SceneNotFoundException(String message) {
        super(message);
    }

    public SceneNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
