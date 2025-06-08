package com.boardgameenjoyers.boardgamebuddy.service.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserWishlistDTO {
    private Long gameId;
    private String gameTitle;
}
