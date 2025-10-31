package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.request.userFavoriteList.AddGameToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGamesResponse;

import java.util.List;

public interface UserFavoriteGamesService {

    List<UserFavoriteGamesResponse> getUserFavoriteGames(Long userId);

    void addGameToUserFavoriteList(AddGameToUserFavoriteListRequest addGameToUserFavoriteListRequest);

    void removeGameFromUserFavoriteList(Long gameId);

    void createUserFavoriteGamesList(User user);
}
