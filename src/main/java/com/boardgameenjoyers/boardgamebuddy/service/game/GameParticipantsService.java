package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameParticipantToGameEntryRequest;

public interface GameParticipantsService {
    void addGameParticipantToGameEntry(AddGameParticipantToGameEntryRequest request, Long gameEntryId);

    void delete(Long id);
}
