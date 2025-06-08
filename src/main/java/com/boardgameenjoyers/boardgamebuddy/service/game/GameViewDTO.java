package com.boardgameenjoyers.boardgamebuddy.service.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameViewDTO {
    private Long gameId;
    private String gameTitle;
    private Long viewCounts;
}
