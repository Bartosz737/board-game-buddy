package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GameEntryMapper {

    public GameEntryDTO toDTO(GameEntry gameEntry) {
        Long id = gameEntry.getId();
        LocalDateTime created = gameEntry.getCreated();
        String gameEntryTitle = gameEntry.getEntryTitle();
        Long gameId = gameEntry.getGame().getId();
        Long groupId = gameEntry.getGroup().getId();

        return new GameEntryDTO(id, created, gameEntryTitle, gameId, groupId);
    }
}
