package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.service.request.UserWishlistCreationRequest;

import java.util.List;

public interface UserWishlistService {

    void AddGameToWishlist(UserWishlistCreationRequest userWishlistCreationRequest);

    void removeGameFromWishlist(Long gameId);

    List<UserWishlistDTO> getUserWishlist();

}
