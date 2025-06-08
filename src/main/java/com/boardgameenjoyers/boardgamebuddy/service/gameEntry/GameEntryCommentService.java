package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import java.util.List;


public interface GameEntryCommentService {

    List<GameEntryCommentDTS> getCommentsForGameEntry(Long gameEntryId);

    void addComment(Long gameEntryId, String text);

    void deleteComment(Long id);
}
