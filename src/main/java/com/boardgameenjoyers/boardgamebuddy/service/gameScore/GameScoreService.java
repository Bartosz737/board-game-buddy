package com.boardgameenjoyers.boardgamebuddy.service.gameScore;

import com.boardgameenjoyers.boardgamebuddy.service.request.GameScoreRequest;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserScoreDTO;

import java.util.List;

public interface GameScoreService {

    void addScore(GameScoreRequest gameScoreRequest, String username);

    void removeScore(Long gameId, String username);

    List<GameScoreDTO> getScoreForGame(Long gameId);

    List<UserScoreDTO> getAllUserScores(String username);

    Double getAverageScoreForGame(Long gameId);

    List<TopGameScoreDTO> getTopFiveGames();

}
