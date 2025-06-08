package com.boardgameenjoyers.boardgamebuddy.service.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class GameDTO {
    private Long gameId;
    private String gameTitle;
    private String description;
    private Integer minPlayers;
    private Integer maxPLayers;
    private Integer age;
    private Integer gameTime;
}
