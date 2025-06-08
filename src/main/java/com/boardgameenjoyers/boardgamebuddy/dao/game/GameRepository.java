package com.boardgameenjoyers.boardgamebuddy.dao.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT COUNT(g) FROM GameEntry g WHERE g.game = :game AND g.created >= :oneMonthAgo")
    Long countGamesInGameEntriesLastMonth(@Param("game") Game game, @Param("oneMonthAgo") LocalDateTime oneMonthAgo);
}
