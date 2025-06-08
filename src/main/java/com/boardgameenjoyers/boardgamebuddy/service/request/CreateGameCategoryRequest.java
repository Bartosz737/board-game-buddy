package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateGameCategoryRequest {
    private final Long gameCategoryId;
    private final String gameCategoryName;
    private final String gameCategoryDescription;
}
