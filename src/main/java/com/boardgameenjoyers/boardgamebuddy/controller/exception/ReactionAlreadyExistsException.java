package com.boardgameenjoyers.boardgamebuddy.controller.exception;

public class ReactionAlreadyExistsException extends RuntimeException {
    public ReactionAlreadyExistsException(String message) {
        super(message);
    }
}
