package com.boardgameenjoyers.boardgamebuddy.service.user;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDTO {
    private final Long id;
    private final String userName;
    private final String email;
    private final LocalDateTime created;
    private final String password;

    public UserDTO(Long id, String userName, String email, String password, LocalDateTime created) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.created = created;
    }
}

