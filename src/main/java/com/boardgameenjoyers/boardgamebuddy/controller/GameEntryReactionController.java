package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.ReactionType;
import com.boardgameenjoyers.boardgamebuddy.service.gameEntry.GameEntryReactionService;
import com.boardgameenjoyers.boardgamebuddy.service.request.GameEntryReactionRequest;
import com.boardgameenjoyers.boardgamebuddy.service.user.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-entries")
@CrossOrigin
public class GameEntryReactionController {
    private final CurrentUserService currentUserService;
    private final GameEntryReactionService gameEntryReactionService;

    @PostMapping("{gameEntryId}/reactions")
    public ResponseEntity<Void> addReaction(@PathVariable long gameEntryId, @RequestBody GameEntryReactionRequest gameEntryReactionRequest) {
        String username = currentUserService.getUsername();
        gameEntryReactionService.addReaction(gameEntryId, username, gameEntryReactionRequest.getReactionType());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{gameEntryId}/reactions")
    public ResponseEntity<Void> removeReaction(@PathVariable long gameEntryId, @RequestBody GameEntryReactionRequest gameEntryReactionRequest) {
        String username = currentUserService.getUsername();
        gameEntryReactionService.removeReaction(gameEntryId, username, gameEntryReactionRequest.getReactionType());
        return ResponseEntity.ok().build();
    }

    @GetMapping("{gameEntryId}/reactions")
    public ResponseEntity<Map<ReactionType, Long>> getReactions(@PathVariable long gameEntryId) {
        Map<ReactionType, Long> reactionCounts = gameEntryReactionService.countReactions(gameEntryId);
        return ResponseEntity.ok(reactionCounts);
    }
}
