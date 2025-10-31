package com.boardgameenjoyers.boardgamebuddy.service.request.userFavoriteList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddGameCategoryToUserFavoriteListRequest {
    private Long gameCategoryId;
}
