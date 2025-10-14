package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.UserFavoriteGameCategory;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGameCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserFavoriteGameCategoryMapper {
    @Mapping(target = "gameCategoryName", source = "gameCategory.gameCategoryName")
    @Mapping(target = "gameCategoryId", source = "gameCategory.id")
    UserFavoriteGameCategoryResponse toResponse(UserFavoriteGameCategory userFavoriteGameCategory);
}
