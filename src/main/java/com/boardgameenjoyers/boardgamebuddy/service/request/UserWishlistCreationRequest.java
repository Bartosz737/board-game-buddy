package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserWishlistCreationRequest {

    private Long gameId;
}
