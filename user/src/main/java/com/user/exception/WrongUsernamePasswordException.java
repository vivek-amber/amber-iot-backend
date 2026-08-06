package com.user.exception;

public class WrongUsernamePasswordException extends RuntimeException{

    public WrongUsernamePasswordException(String message) {
        super(message);
    }

    public WrongUsernamePasswordException(String message, Throwable cause) {
        super(message,cause);
    }
}
