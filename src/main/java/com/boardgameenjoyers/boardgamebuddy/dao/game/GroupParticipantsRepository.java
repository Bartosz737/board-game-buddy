package com.boardgameenjoyers.boardgamebuddy.dao.game;

import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupParticipantsRepository extends JpaRepository<GroupParticipants, Long> {
    List<GroupParticipants> findByGroupId(Long groupId);
}

