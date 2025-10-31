package com.boardgameenjoyers.boardgamebuddy.service.request;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.GameType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class CreateGameEntryRequest {

    private final String entryTitle;
    private final String description;
    private final Long gameId;
    private final Long groupId;
    private final GameType gameType;

    @Getter
    @RequiredArgsConstructor
    public static class GameParticipantsData {
        private final Long groupParticipantId;
        private final Long points;
    }
}
