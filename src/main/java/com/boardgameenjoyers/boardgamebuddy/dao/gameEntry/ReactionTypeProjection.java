package com.boardgameenjoyers.boardgamebuddy.dao.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.ReactionType;

public interface ReactionTypeProjection {
    ReactionType getType();

    Long getCount();
}
