package com.boardgameenjoyers.boardgamebuddy.controller.exception;

public class AlreadyScoreExistsException extends RuntimeException {
    public AlreadyScoreExistsException(String message) {
        super(message);
    }
}
