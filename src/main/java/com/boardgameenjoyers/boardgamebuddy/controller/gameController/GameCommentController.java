package com.boardgameenjoyers.boardgamebuddy.controller.gameController;

import com.boardgameenjoyers.boardgamebuddy.service.game.GameCommentService;
import com.boardgameenjoyers.boardgamebuddy.service.game.GameCommentDTS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("game-comment")
@CrossOrigin
public class GameCommentController {
    private final GameCommentService gameCommentService;

    @GetMapping("{gameId}")
    public ResponseEntity<List<GameCommentDTS>> getGameComments(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameCommentService.getCommentsForGame(gameId));
    }

    @PostMapping("{gameId}")
    public ResponseEntity<Void> addComment(@PathVariable Long gameId, @RequestBody String text) {
        gameCommentService.addComment(gameId, text);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteGameComment(@PathVariable Long id) {
        gameCommentService.deleteGameComment(id);
        return ResponseEntity.ok().build();
    }
}
