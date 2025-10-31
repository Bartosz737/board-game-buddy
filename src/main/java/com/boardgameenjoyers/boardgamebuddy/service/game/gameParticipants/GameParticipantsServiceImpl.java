package com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.service.event.GameParticipantRemoveEvent;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


@Service
@RequiredArgsConstructor
public class GameParticipantsServiceImpl implements GameParticipantsService {

    private final GameParticipantsRepository gameParticipantsRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameEntryRepository gameEntryRepository;
    private final EntityOwnershipChecker entityOwnershipChecker;


    @Override
    public void delete(Long gameEntryId, Long userId) {


        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Game entry with id: " + gameEntryId + " not found"));
        validateGameEntryOwner(gameEntry);

        GameParticipants participants = gameParticipantsRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));

        gameParticipantsRepository.delete(participants);
        applicationEventPublisher.publishEvent(new GameParticipantRemoveEvent(participants.getUser().getId()));
    }

    private void validateGameEntryOwner(GameEntry gameEntry) {
        if (!entityOwnershipChecker.isCurrentUserOwner(gameEntry.getCreatedBy())) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized operation.");
        }
    }
}
