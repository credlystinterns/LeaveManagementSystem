package com.example.lmsapplication.exception;




public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String token) {
        super("Session not found or expired for token");
    }
}

