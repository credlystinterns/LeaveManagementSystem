package com.example.lmsapplication.exception;

public class LeavesNotFoundException extends RuntimeException {
    public LeavesNotFoundException(String message) {
        super(message);
    }
    public LeavesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
