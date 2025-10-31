package com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.TeamOutcome;

public record AddGameParticipantToCooperativeGameEntryRequest(Long userId, Long groupParticipantId,
                                                              TeamOutcome outcome) {
}
