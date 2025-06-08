package com.boardgameenjoyers.boardgamebuddy.service.gameCategory;

import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategory;
import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameToCategoryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.CreateGameCategoryRequest;


import java.util.List;

public interface GameCategoryService {

    List<GameCategoryDTO> getAllgamesCategory();

    List<GameCategoryLinkResponse> getAllGamesInCategory(Long gameCategoryId);

    GameCategoryDTO createGameCategory(CreateGameCategoryRequest createGameCategoryRequest);

    GameCategoryDTO getGameCategoryByName(Long gameCategoryId);

    GameCategoryDTO addGameToGameCategory(AddGameToCategoryRequest addGameToCategoryRequest);

    GameCategory getGameCategoryById(Long gameCategoryId);
}
