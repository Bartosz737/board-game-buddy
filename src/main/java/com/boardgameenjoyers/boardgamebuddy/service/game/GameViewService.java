package com.boardgameenjoyers.boardgamebuddy.service.game;


public interface GameViewService {

    void recordView(Long gameId);

    GameViewDTO getGameViews(Long gameId);
}
