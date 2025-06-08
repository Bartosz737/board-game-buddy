package com.boardgameenjoyers.boardgamebuddy.controller.exception;

public class WrongReactionException extends RuntimeException {
    public WrongReactionException(String message) {
        super(message);
    }
}
