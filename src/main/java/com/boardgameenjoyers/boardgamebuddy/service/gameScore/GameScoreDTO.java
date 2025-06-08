package com.boardgameenjoyers.boardgamebuddy.service.gameScore;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameScoreDTO {
    private Long gameId;
    private String userName;
    private int rating;
    private LocalDateTime ratedAt;

}
