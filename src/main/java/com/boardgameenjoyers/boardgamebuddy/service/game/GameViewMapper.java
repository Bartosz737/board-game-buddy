package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameView;
import org.springframework.stereotype.Component;

@Component
public class GameViewMapper {

    public GameViewDTO toDTO(Game game, GameView gameView) {
        return new GameViewDTO(
                game.getId(),
                game.getGameTitle(),
                gameView.getCountView()
        );
    }
}
