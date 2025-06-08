package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.service.game.GameParticipantsService;
import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameParticipantToGameEntryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("game-participants")
@CrossOrigin
public class GameParticipantController {
    private final GameParticipantsService gameParticipantsService;

    @PostMapping("{gameEntryId}")
    public ResponseEntity<Void> addGameParticipantToGameEntry(@RequestBody AddGameParticipantToGameEntryRequest request, @PathVariable Long gameEntryId) {
        gameParticipantsService.addGameParticipantToGameEntry(request, gameEntryId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("{gameEntryId}/{id}")
    public ResponseEntity<Void> deleteGameParticipant(@PathVariable Long id) {
        gameParticipantsService.delete(id);
        return ResponseEntity.ok().build();
    }
}
