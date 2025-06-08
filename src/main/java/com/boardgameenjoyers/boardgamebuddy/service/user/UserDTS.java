package com.boardgameenjoyers.boardgamebuddy.service.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserDTS {

    private final Long id;
    private final String userName;
    private final String email;

    private final LocalDateTime created;
}

