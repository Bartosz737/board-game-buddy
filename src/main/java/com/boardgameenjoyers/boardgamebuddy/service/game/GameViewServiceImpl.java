package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameView;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GameViewServiceImpl implements GameViewService {

    private final GameRepository gameRepository;
    private final GameViewRepository gameViewRepository;
    private final GameViewMapper gameViewMapper;

    @Override
    @Transactional
    public void recordView(Long gameId) {
        Game game = gameRepository.findById(gameId).
                orElseThrow(() -> new EntityNotFoundException("game not found"));

        GameView view = gameViewRepository.findByGame(game).orElseGet(() -> {
            GameView newView = new GameView();
            newView.setGame(game);
            return newView;
        });
        view.setCountView(view.getCountView() + 1);
        gameViewRepository.save(view);
    }

    @Override
    public GameViewDTO getGameViews(Long gameId) {
        Game game = gameRepository.findById(gameId).
                orElseThrow(() -> new EntityNotFoundException("game not found"));

        GameView view = gameViewRepository.findByGame(game).orElse(new GameView());
        return gameViewMapper.toDTO(game, view);
    }
}
