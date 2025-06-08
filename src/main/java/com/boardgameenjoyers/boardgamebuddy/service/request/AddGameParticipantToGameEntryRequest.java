package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddGameParticipantToGameEntryRequest {
    private final Long userId;
    private final Long groupParticipantId;
    private final Long points;
}
