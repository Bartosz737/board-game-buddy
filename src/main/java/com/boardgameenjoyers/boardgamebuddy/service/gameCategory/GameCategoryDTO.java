package com.boardgameenjoyers.boardgamebuddy.service.gameCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameCategoryDTO {
    private Long gameCategoryId;
    private String gameCategoryName;
    private String gameCategoryDescription;
}
