package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class EditGameEntryRequest {

    private final Long gameEntryId;
    private final String entryTitle;
    private final String description;
}
