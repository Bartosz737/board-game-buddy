package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameScoreRequest {
    private int rating;
}
