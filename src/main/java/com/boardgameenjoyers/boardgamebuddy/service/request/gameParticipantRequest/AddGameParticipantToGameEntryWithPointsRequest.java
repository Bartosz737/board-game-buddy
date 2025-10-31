package com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest;

public record AddGameParticipantToGameEntryWithPointsRequest(Long userId, Long groupParticipantId, Long points) {
}
