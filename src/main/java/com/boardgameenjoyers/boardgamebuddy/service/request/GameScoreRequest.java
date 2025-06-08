package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameScoreRequest {

    private Long gameId;
    private String username;
    private int rating;
}
