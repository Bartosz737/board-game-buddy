package com.boardgameenjoyers.boardgamebuddy.dao.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameEntryRepository extends JpaRepository<GameEntry, Long> {
    List<GameEntry> findAllByGroup_Id(Long groupId);

    @Query("SELECT ge FROM GameEntry ge WHERE ge.game = :game")
    List<GameEntry> findAllGameEntriesWithGame(@Param("game") Game game);
}
