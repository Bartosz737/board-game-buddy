package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.UserFavoriteGames;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGamesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserFavoriteGamesMapper {

    @Mapping(target = "gameTitle", source = "game.gameTitle")
    @Mapping(target = "gameId", source = "game.id")
    UserFavoriteGamesResponse toResponse(UserFavoriteGames userFavoriteGames);
}
