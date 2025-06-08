package com.boardgameenjoyers.boardgamebuddy.dao.gameEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameEntryReactionRepository extends JpaRepository<GameEntryReaction, Long> {
    @Query("SELECT r.reactionType AS type, COUNT(r) AS count " +
            "FROM GameEntryReaction r WHERE r.gameEntry.id = :gameEntryId GROUP BY r.reactionType")
    List<ReactionTypeProjection> countByReactionType(@Param("gameEntryId") Long gameEntryId);

    Optional<GameEntryReaction> findByGameEntryIdAndUserUserName(Long gameEntryId, String username);

}
