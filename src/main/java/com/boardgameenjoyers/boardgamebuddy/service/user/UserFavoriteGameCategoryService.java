package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameCategoryToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGameCategoryResponse;

import java.util.List;

public interface UserFavoriteGameCategoryService {
    void addGameCategoryAsFavorite(AddGameCategoryToUserFavoriteListRequest addGameCategoryToUserFavoriteListRequest);

    void removeGameCategoryFromUserFavoriteList(Long gameCategoryId);

    void createUserFavoriteGameCategoryList(User user);

    List<UserFavoriteGameCategoryResponse> getUserFavoriteGameCategoryList(Long userId);
}
