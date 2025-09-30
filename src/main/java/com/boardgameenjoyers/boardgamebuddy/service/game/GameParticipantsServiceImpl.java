package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GroupParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.event.GameParticipantAddedEvent;
import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameParticipantToGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameParticipantsServiceImpl implements GameParticipantsService {

    private final GameParticipantsRepository gameParticipantsRepository;
    private final GroupParticipantsRepository groupParticipantsRepository;
    private final GameEntryRepository gameEntryRepository;
    private final EntityOwnershipChecker entityOwnershipChecker;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    @Transactional
    public void addGameParticipantToGameEntry(AddGameParticipantToGameEntryRequest request, Long gameEntryId) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId).orElseThrow(EntityNotFoundException::new);
        validateGameEntryOwner(gameEntry);

        GroupParticipants groupParticipant = groupParticipantsRepository.findById(request.getGroupParticipantId()).orElseThrow(EntityNotFoundException::new);
        User user = groupParticipant.getUser();

        GameParticipants gameParticipant = new GameParticipants();
        gameParticipant.setUser(user);
        gameParticipant.setPoint(request.getPoints());
        gameParticipant.setGameEntry(gameEntry);
        gameParticipantsRepository.save(gameParticipant);

        applicationEventPublisher.publishEvent(new GameParticipantAddedEvent(user.getId()));
    }

    @Override
    public void delete(Long userId) {
        GameParticipants participants = gameParticipantsRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));

        gameParticipantsRepository.delete(participants);
    }

    private void validateGameEntryOwner(GameEntry gameEntry) {
        if (!entityOwnershipChecker.isCurrentUserOwner(gameEntry.getCreatedBy())) {
            throw new AccessDeniedException("Unauthorized operation.");
        }
    }

    public Optional<GameParticipants> findByUserId(Long userId) {
        return Optional.ofNullable(gameParticipantsRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }
}
