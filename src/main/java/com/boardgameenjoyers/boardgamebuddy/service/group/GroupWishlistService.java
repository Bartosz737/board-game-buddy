package com.boardgameenjoyers.boardgamebuddy.service.group;

import java.util.List;

public interface GroupWishlistService {

    List<GroupWishlistDTS> getWishlist(Long groupId);

    void addGameToGroupWishlist(Long groupId, Long gameId);

    void removeFromWishlist(Long id);
}
