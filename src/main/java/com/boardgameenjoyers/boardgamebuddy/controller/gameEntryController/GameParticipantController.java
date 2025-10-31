package com.boardgameenjoyers.boardgamebuddy.controller.gameEntryController;

import com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants.AddGameParticipantService;
import com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants.GameParticipantsService;
import com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest.AddGameParticipantToCooperativeGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest.AddGameParticipantToGameEntryNoPointsRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest.AddGameParticipantToGameEntryWithPointsRequest;
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
    private final AddGameParticipantService addGameParticipantService;

    @PostMapping("versus-points/{gameEntryId}")
    public ResponseEntity<Void> addGameParticipantToGameEntryVersusPoints(@RequestBody AddGameParticipantToGameEntryWithPointsRequest request,
                                                                          @PathVariable Long gameEntryId) {
        addGameParticipantService.addGameParticipantWithPoints(request, gameEntryId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("versus-no-points/{gameEntryId}")
    public ResponseEntity<Void> addGameParticipantToGameEntryVersusNoPoints(@RequestBody AddGameParticipantToGameEntryNoPointsRequest request,
                                                                            @PathVariable Long gameEntryId) {
        addGameParticipantService.addGameParticipantNoPoints(request, gameEntryId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("cooperative/{gameEntryId}")
    public ResponseEntity<Void> addGameParticipantToGameEntryCooperative(@RequestBody AddGameParticipantToCooperativeGameEntryRequest request,
                                                                         @PathVariable Long gameEntryId) {
        addGameParticipantService.addGameParticipantCooperative(request, gameEntryId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("{gameEntryId}/participant/{userId}")
    public ResponseEntity<Void> deleteGameParticipant(@PathVariable Long gameEntryId, @PathVariable Long userId) {
        gameParticipantsService.delete(gameEntryId, userId);
        return ResponseEntity.ok().build();
    }
}
