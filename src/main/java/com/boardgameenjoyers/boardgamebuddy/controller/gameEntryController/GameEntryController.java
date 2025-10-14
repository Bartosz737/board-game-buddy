package com.boardgameenjoyers.boardgamebuddy.controller.gameEntryController;

import com.boardgameenjoyers.boardgamebuddy.service.gameEntry.*;
import com.boardgameenjoyers.boardgamebuddy.service.request.CreateGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.EditGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.game.GameParticipantsDts;
import com.boardgameenjoyers.boardgamebuddy.service.request.GameEntryWhichUserParticipated;
import com.boardgameenjoyers.boardgamebuddy.service.user.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("game-entry")
@CrossOrigin
public class GameEntryController {
    private final GameEntryService gameEntryService;
    private final CurrentUserService currentUserService;

    @GetMapping("group/{groupId}")
    public ResponseEntity<List<GameEntryDTO>> getGameEntriesByGroup(@PathVariable Long groupId) {
        List<GameEntryDTO> groupGameEntries = gameEntryService.getGameEntriesByGroupId(groupId);
        return ResponseEntity.ok(groupGameEntries);
    }

    @GetMapping("{id}")
    public ResponseEntity<GameEntryDTS> getGameEntry(@PathVariable Long id) {
        return ResponseEntity.ok(gameEntryService.getGameEntry(id));
    }

    @PostMapping("{groupId}/create")
    public ResponseEntity<Void> createGameEntry(@RequestBody CreateGameEntryRequest createGameEntryRequest) {
        gameEntryService.createGameEntry(createGameEntryRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> editGameEntry(@RequestBody EditGameEntryRequest editGameEntryRequest) {
        gameEntryService.editGameEntry(editGameEntryRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{gameEntryId}/gameParticipants")
    public ResponseEntity<List<GameParticipantsDts>> getGameParticipants(@PathVariable Long gameEntryId) {
        List<GameParticipantsDts> gameParticipantsDts = gameEntryService.getGameParticipantsForGameEntry(gameEntryId);
        return ResponseEntity.ok(gameParticipantsDts);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteGameEntry(@PathVariable Long id) {
        gameEntryService.deleteGameEntryById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("user/{userName}")
    public ResponseEntity<List<GameEntryWhichUserParticipated>> getGameEntriesByUser(@PathVariable String userName) {
        List<GameEntryWhichUserParticipated> gameEntries = gameEntryService.findAllGameEntriesByUserByOrderCreatedDesc(userName);
        return ResponseEntity.ok(gameEntries);
    }
}

