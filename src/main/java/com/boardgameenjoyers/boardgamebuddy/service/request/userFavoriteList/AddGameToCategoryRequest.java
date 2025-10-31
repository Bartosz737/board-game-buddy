package com.boardgameenjoyers.boardgamebuddy.service.request.userFavoriteList;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddGameToCategoryRequest {
    private Long gameId;
    private Long gameCategoryId;
}
