package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameDTO toDTO(Game game) {
        Long gameId = game.getId();
        String gameTitle = game.getGameTitle();
        String description = game.getDescription();
        Integer minPlayers = game.getMinPlayers();
        Integer maxPlayers = game.getMaxPLayers();
        Integer age = game.getAge();
        Integer gameTime = game.getGameTime();

        return new GameDTO(gameId, gameTitle, description, minPlayers, maxPlayers, age, gameTime);
    }
}
