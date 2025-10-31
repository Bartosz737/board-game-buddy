package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants.GameParticipantsDts;
import com.boardgameenjoyers.boardgamebuddy.service.request.CreateGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.EditGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest.GameEntryWhichUserParticipated;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameEntryService {
    void deleteGameEntryById(Long gameEntryId);

    List<GameEntryDTO> getGameEntriesByGroupId(Long groupId);

    List<GameParticipantsDts> getGameParticipantsForGameEntry(Long gameEntryId);

    GameEntryDTS getGameEntry(Long gameEntryId);

    void createGameEntry(CreateGameEntryRequest createGameEntryRequest);

    void editGameEntry(EditGameEntryRequest editGameEntryRequest);

    @Query("SELECT p.gameEntry FROM GameParticipants p WHERE  p.user.userName = :userName ORDER BY p.gameEntry.created DESC")
    List<GameEntryWhichUserParticipated> findAllGameEntriesByUserByOrderCreatedDesc(@Param("userName") String userName);
}
