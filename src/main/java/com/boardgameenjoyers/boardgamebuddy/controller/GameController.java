package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.service.game.*;
import com.boardgameenjoyers.boardgamebuddy.service.gameCategory.GameCategoryService;
import com.boardgameenjoyers.boardgamebuddy.service.gameCategory.GameCategoryLinkResponse;
import com.boardgameenjoyers.boardgamebuddy.service.request.GamesPlayedPerMonth;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("game")
@CrossOrigin
public class GameController implements Serializable {
    private GameService gameService;
    private GameCategoryService gameCategoryService;
    private GameViewService gameViewService;

    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getAllGames() {
        return ResponseEntity.ok(gameService.findAllGames());
    }

    @GetMapping("{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGameById(id));
    }

    @PostMapping("create")
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO gameDTO) {
        GameDTO createdGame = gameService.createNewGame(gameDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
    }

    @GetMapping("category/{gameCategoryId}")
    public ResponseEntity<List<GameCategoryLinkResponse>> getGamesInCategory(@PathVariable Long gameCategoryId) {
        List<GameCategoryLinkResponse> games = gameCategoryService.getAllGamesInCategory(gameCategoryId);
        return ResponseEntity.ok(games);
    }

    @GetMapping("games/month/{gameId}")
    public ResponseEntity<GamesPlayedPerMonth> getGamesPlayedPerMonth(@PathVariable Long gameId) {
        GamesPlayedPerMonth gamesPlayedPerMonths = gameService.getGamesPlayedPerMonth(gameId);
        return ResponseEntity.ok(gamesPlayedPerMonths);
    }

    @GetMapping("{gameId}/views")
    public ResponseEntity<GameViewDTO> getGameViews(@PathVariable Long gameId) {
        GameViewDTO views = gameViewService.getGameViews(gameId);
        return ResponseEntity.ok(views);
    }

}
