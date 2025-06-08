package com.boardgameenjoyers.boardgamebuddy.controller.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}


