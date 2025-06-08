package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.service.gameScore.GameScoreDTO;
import com.boardgameenjoyers.boardgamebuddy.service.gameScore.GameScoreService;
import com.boardgameenjoyers.boardgamebuddy.service.gameScore.TopGameScoreDTO;
import com.boardgameenjoyers.boardgamebuddy.service.request.GameScoreRequest;
import com.boardgameenjoyers.boardgamebuddy.service.user.CurrentUserService;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserScoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("game-score")
@CrossOrigin
public class GameScoreController {
    private final GameScoreService gameScoreService;
    private final CurrentUserService currentUserService;

    @PostMapping("/score")
    public ResponseEntity<GameScoreRequest> addScore(@RequestBody GameScoreRequest gameScoreRequest) {
        String username = currentUserService.getUsername();
        gameScoreService.addScore(gameScoreRequest, username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{gameId}")
    public ResponseEntity<Void> removeScore(@PathVariable Long gameId) {
        String username = currentUserService.getUsername();
        gameScoreService.removeScore(gameId, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{gameId}")
    public ResponseEntity<List<GameScoreDTO>> getGameScore(@PathVariable Long gameId) {
        List<GameScoreDTO> score = gameScoreService.getScoreForGame(gameId);
        return ResponseEntity.ok(score);
    }

    @GetMapping("average/{gameId}")
    public ResponseEntity<Double> getGameAverageScore(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameScoreService.getAverageScoreForGame(gameId));
    }

    @GetMapping("user/{username}/scores")
    public ResponseEntity<List<UserScoreDTO>> getUserScores(@PathVariable String username) {
        return ResponseEntity.ok(gameScoreService.getAllUserScores(username));
    }

    @GetMapping("top")
    public ResponseEntity<List<TopGameScoreDTO>> getTop5Games(@RequestBody TopGameScoreDTO topGameScoreDTO) {
        return ResponseEntity.ok(gameScoreService.getTopFiveGames());
    }
}
