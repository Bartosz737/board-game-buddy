package com.boardgameenjoyers.boardgamebuddy.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final String message;
    private final String details;
    private final int StatusCode;
}
