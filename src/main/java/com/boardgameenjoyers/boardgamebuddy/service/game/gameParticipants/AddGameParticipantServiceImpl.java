package com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.GameType;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GroupParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.event.GameParticipantAddedEvent;
import com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest.AddGameParticipantToCooperativeGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest.AddGameParticipantToGameEntryNoPointsRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.gameParticipantRequest.AddGameParticipantToGameEntryWithPointsRequest;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AddGameParticipantServiceImpl implements AddGameParticipantService {

    private final GroupParticipantsRepository groupParticipantsRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameEntryRepository gameEntryRepository;
    private final GameParticipantsRepository gameParticipantsRepository;
    private final EntityOwnershipChecker entityOwnershipChecker;


    @Override
    public void addGameParticipantWithPoints(AddGameParticipantToGameEntryWithPointsRequest request, Long gameEntryId) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new EntityNotFoundException("Game entry with id: " + gameEntryId + " not found"));

        validateGameEntryOwner(gameEntry);

        GroupParticipants groupParticipant = groupParticipantsRepository.findById(request.groupParticipantId())
                .orElseThrow(() -> new EntityNotFoundException
                        ("Group participant with id: " + request.groupParticipantId() + " is not a member of the group or do not exists"));
        User user = groupParticipant.getUser();

        if (gameParticipantsRepository.existsByGameEntryIdAndUserId(gameEntryId, user.getId())) {
            throw new IllegalArgumentException
                    ("User " + user.getId() + " is already a participant for game entry: " + gameEntryId);
        }

        GameParticipants gameParticipant = new GameParticipants();
        gameParticipant.setUser(user);
        gameParticipant.setGameEntry(gameEntry);
        gameParticipant.setPoint(request.points());
        gameParticipant.setTeamOutcome(null);
        gameParticipantsRepository.save(gameParticipant);

        applicationEventPublisher.publishEvent(new GameParticipantAddedEvent(user.getId()));
    }

    @Override
    public void addGameParticipantNoPoints(AddGameParticipantToGameEntryNoPointsRequest request, Long gameEntryId) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new EntityNotFoundException("Game entry with id: " + gameEntryId + " not found"));

        validateGameEntryOwner(gameEntry);

        GroupParticipants groupParticipant = groupParticipantsRepository.findById(request.groupParticipantId())
                .orElseThrow(() -> new EntityNotFoundException
                        ("Group participant with id: " + request.groupParticipantId() + " is not a member of the group or do not exists"));
        User user = groupParticipant.getUser();

        if (gameParticipantsRepository.existsByGameEntryIdAndUserId(gameEntryId, user.getId())) {
            throw new IllegalArgumentException("User " + user.getId() + " is already a participant for game entry: " + gameEntryId);
        }

        GameParticipants gameParticipant = new GameParticipants();
        gameParticipant.setUser(user);
        gameParticipant.setGameEntry(gameEntry);
        gameParticipant.setTeamOutcome(null);
        gameParticipantsRepository.save(gameParticipant);

        applicationEventPublisher.publishEvent(new GameParticipantAddedEvent(user.getId()));
    }

    @Override
    public void addGameParticipantCooperative(AddGameParticipantToCooperativeGameEntryRequest request, Long gameEntryId) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new EntityNotFoundException("Game entry with id: " + gameEntryId + " not found"));

        validateGameEntryOwner(gameEntry);

        GroupParticipants groupParticipant = groupParticipantsRepository.findById(request.groupParticipantId())
                .orElseThrow(() -> new EntityNotFoundException
                        ("Group participant with id: " + request.groupParticipantId() + " is not a member of the group or do not exists"));
        User user = groupParticipant.getUser();

        if (gameParticipantsRepository.existsByGameEntryIdAndUserId(gameEntryId, user.getId())) {
            throw new IllegalArgumentException("User " + user.getId() + " is already a participant for game entry: " + gameEntryId);
        }

        GameParticipants gameParticipant = new GameParticipants();
        gameParticipant.setUser(user);
        gameParticipant.setGameEntry(gameEntry);
        gameParticipant.setTeamOutcome(request.outcome());
        gameParticipantsRepository.save(gameParticipant);

        applicationEventPublisher.publishEvent(new GameParticipantAddedEvent(user.getId()));
    }

    private void validateGameEntryOwner(GameEntry gameEntry) {
        if (!entityOwnershipChecker.isCurrentUserOwner(gameEntry.getCreatedBy())) {
            throw new AccessDeniedException("Unauthorized operation.");
        }
    }
}
