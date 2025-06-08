package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.ReactionType;

import java.util.Map;

public interface GameEntryReactionService {
    void addReaction(Long gameEntryId, String username, ReactionType reactionType);

    void removeReaction(Long gameEntryId, String username, ReactionType reactionType);

    Map<ReactionType, Long> countReactions(Long gameEntryId);
}
