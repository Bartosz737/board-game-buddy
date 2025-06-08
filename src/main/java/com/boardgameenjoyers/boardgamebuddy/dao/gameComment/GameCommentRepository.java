package com.boardgameenjoyers.boardgamebuddy.dao.gameComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameCommentRepository extends JpaRepository<GameComment, Long> {
    List<GameComment> findByGameId(Long gameId);
}
