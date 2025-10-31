package com.boardgameenjoyers.boardgamebuddy.service.request.utilRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequest {

    private final String username;
    private final String password;
}
