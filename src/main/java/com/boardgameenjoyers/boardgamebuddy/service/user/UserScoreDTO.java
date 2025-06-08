package com.boardgameenjoyers.boardgamebuddy.service.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserScoreDTO {
    private Long gameId;
    private int rating;
    private LocalDateTime ratedAt;
}
