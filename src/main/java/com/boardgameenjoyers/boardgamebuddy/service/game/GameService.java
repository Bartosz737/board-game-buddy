package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.service.request.GamesPlayedPerMonth;

import java.util.List;

public interface GameService {

    GameDTO getGameById(Long id);

    GamesPlayedPerMonth getGamesPlayedPerMonth(Long gameId);

    GameDTO createNewGame(GameDTO gameDTO);

    List<GameDTO> findAllGames();
}
