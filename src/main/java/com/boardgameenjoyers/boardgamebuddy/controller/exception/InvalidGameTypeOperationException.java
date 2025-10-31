package com.boardgameenjoyers.boardgamebuddy.controller.exception;

public class InvalidGameTypeOperationException extends RuntimeException {
    public InvalidGameTypeOperationException(String message) {
        super(message);
    }
}
