package com.boardgameenjoyers.boardgamebuddy.service.gameScore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopGameScoreDTO {
    private Long gameId;
    private double averageRating;
}
