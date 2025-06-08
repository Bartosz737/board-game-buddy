package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GameEntryCommentDTS {

    private Long id;
    private String text;
    private LocalDateTime created;
    private UserData user;
    private boolean owner;

    @Getter
    @RequiredArgsConstructor
    public static class UserData {
        private final Long id;
        private final String userName;
        private final String email;
    }
}
