package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.service.gameEntry.GameEntryCommentService;
import com.boardgameenjoyers.boardgamebuddy.service.gameEntry.GameEntryCommentDTS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("game-entry-comment")
@CrossOrigin
public class GameEntryCommentController {
    private final GameEntryCommentService gameEntryCommentService;

    @GetMapping("{gameEntryId}")
    public ResponseEntity<List<GameEntryCommentDTS>> getCommentsForGameEntry(@PathVariable Long gameEntryId) {
        return ResponseEntity.ok(gameEntryCommentService.getCommentsForGameEntry(gameEntryId));
    }

    @PostMapping("{gameEntryId}")
    public ResponseEntity<Void> addComment(@PathVariable Long gameEntryId, @RequestBody String text) {
        gameEntryCommentService.addComment(gameEntryId, text);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        gameEntryCommentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}
