package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.service.gameEntry.GameEntryCommentDTS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GameCommentDTS {

    private final Long id;
    private final String text;
    private final LocalDateTime created;
    private final GameEntryCommentDTS.UserData user;
    private final boolean owner;

    @Getter
    @RequiredArgsConstructor
    public static class UserData {
        private final Long id;
        private final String userName;
        private final String email;
    }
}
