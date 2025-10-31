package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants.GameParticipantsDts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GameEntryDTS {
    private final Long id;
    private final LocalDateTime created;
    private final String entryTitle;
    private final String gameTitle;
    private final String description;
    private final String groupName;
    private final List<GameParticipantsDts> gameParticipants;
    private final boolean owner;
}
