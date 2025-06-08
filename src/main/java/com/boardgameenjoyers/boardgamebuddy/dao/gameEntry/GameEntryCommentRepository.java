package com.boardgameenjoyers.boardgamebuddy.dao.gameEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameEntryCommentRepository extends JpaRepository<GameEntryComment, Long> {
    List<GameEntryComment> findByGameEntryId(Long gameEntryId);
}
