package com.boardgameenjoyers.boardgamebuddy.dao.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GameViewRepository extends JpaRepository<GameView, Long> {
    Optional<GameView> findByGame(Game game);
}
