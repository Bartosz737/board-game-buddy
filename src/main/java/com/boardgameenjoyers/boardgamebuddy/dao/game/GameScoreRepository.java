package com.boardgameenjoyers.boardgamebuddy.dao.game;

import com.boardgameenjoyers.boardgamebuddy.service.gameScore.TopGameScoreDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameScoreRepository extends JpaRepository<GameScore, Long> {
    Optional<GameScore> findByGameIdAndUserUserName(Long gameId, String username);

    List<GameScore> findAllByUser_UserName(String username);

    List<GameScore> findAllByGameId(Long gameId);

    @Query("SELECT AVG(gs.rating) FROM GameScore gs WHERE gs.game.id = :gameId")
    Double findAverageScoreForGame(@Param("gameId") Long gameId);

    @Query("SELECT new com.boardgameenjoyers.boardgamebuddy.service.gameScore.TopGameScoreDTO(g.id, AVG(s.rating)) " +
            "FROM GameScore s JOIN s.game g " +
            "GROUP BY g.id " +
            "ORDER BY AVG(s.rating) DESC")
    List<TopGameScoreDTO> findTop5GamesByAverageRating(Pageable pageable);
}
