package com.boardgameenjoyers.boardgamebuddy.service.game;

import java.util.List;

public interface GameCommentService {

    List<GameCommentDTS> getCommentsForGame(Long gameId);

    void addComment(Long gameId, String text);

    void deleteGameComment(Long id);
}
