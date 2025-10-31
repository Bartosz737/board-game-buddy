package com.boardgameenjoyers.boardgamebuddy.service.request.utilRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterAdminRequest {
    private final String userName;
    private final String email;
    private final String password;
}
