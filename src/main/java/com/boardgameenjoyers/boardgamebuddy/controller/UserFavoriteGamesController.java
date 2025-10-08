package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGamesResponse;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserFavoriteGamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-favorite-games")
public class UserFavoriteGamesController {

    private final UserFavoriteGamesService userFavoriteGamesService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserFavoriteGamesResponse>> getUserFavoriteGamesList(@PathVariable Long userId) {
        return ResponseEntity.ok(userFavoriteGamesService.getUserFavoriteGames(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<List<AddGameToUserFavoriteListRequest>> addGameToUserFavoriteList(@RequestBody AddGameToUserFavoriteListRequest addGameToUserFavoriteListRequest) {
        userFavoriteGamesService.addGameToUserFavoriteList(addGameToUserFavoriteListRequest);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{gameId}")
    public ResponseEntity<?> removeGameFromUserFavoriteList(@PathVariable long gameId){
        userFavoriteGamesService.removeGameFromUserFavoriteList(gameId);
        return  ResponseEntity.ok().build();
    }

}
