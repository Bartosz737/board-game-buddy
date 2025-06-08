package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GamesPlayedPerMonth {
    private final Long gameId;
    private final String gameTitle;
    private final Long gamesPlayed;
}
