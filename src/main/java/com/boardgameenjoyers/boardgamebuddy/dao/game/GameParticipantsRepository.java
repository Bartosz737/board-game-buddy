package com.boardgameenjoyers.boardgamebuddy.dao.game;

import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameParticipantsRepository extends JpaRepository<GameParticipants, Long> {
    List<GameParticipants> findByGameEntryId(Long gameEntryId);

    Optional<GameParticipants> findByUserId(Long userId);

    List<GameParticipants> findByGameEntry(GameEntry gameEntry);

    List<GameParticipants> findGameEntriesByUser(User user);

    long countByUserProfileUserId(Long userId);

    boolean existsByGameEntryIdAndUserId(Long gameEntryId, Long userId);
}