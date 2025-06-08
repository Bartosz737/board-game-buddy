package com.boardgameenjoyers.boardgamebuddy.service.gameScore;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameScore;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GameScoreMapper {

    public GameScoreDTO toDTO(GameScore gameScore) {
        Long gameId = gameScore.getGame().getId();
        String username = gameScore.getUser().getUserName();
        int score = gameScore.getRating();
        LocalDateTime dateTime = gameScore.getDateTime();

        return new GameScoreDTO(gameId, username, score, dateTime);
    }
}
