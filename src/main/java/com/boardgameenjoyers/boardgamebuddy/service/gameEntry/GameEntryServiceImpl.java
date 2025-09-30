package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.group.Group;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.service.game.GameParticipantsDts;
import com.boardgameenjoyers.boardgamebuddy.service.request.CreateGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.EditGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.GameEntryWhichUserParticipated;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class GameEntryServiceImpl implements GameEntryService {
    private final GameRepository gameRepository;
    private final GroupRepository groupRepository;
    private final GameEntryRepository gameEntryRepository;
    private final UserMapper userMapper;
    private final GameParticipantsRepository gameParticipantsRepository;
    private final EntityOwnershipChecker entityOwnershipChecker;
    private final UserRepository userRepository;
    private final GameEntryMapper gameEntryMapper;


    @Override
    @Transactional
    public void deleteGameEntryById(Long gameEntryId) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId).orElseThrow(EntityNotFoundException::new);
        validateGameEntryOwner(gameEntry);

        gameEntryRepository.delete(gameEntry);
    }

    @Override
    public List<GameEntryDTO> getGameEntriesByGroupId(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new EntityNotFoundException("Group with id: " + groupId + " not found");
        }

        List<GameEntry> groupGameEntries = gameEntryRepository.findAllByGroup_Id(groupId);

        if (groupGameEntries.isEmpty()) {
            throw new EntityNotFoundException("There is no game entries in this group with id: " + groupId);
        }

        return groupGameEntries.stream().map(gameEntryMapper::toDTO).collect(toList());
    }

    @Override
    public List<GameParticipantsDts> getGameParticipantsForGameEntry(Long gameEntryId) {
        List<GameParticipants> gameParticipants = gameParticipantsRepository.findByGameEntryId(gameEntryId);

        return mapToGameParticipantDts(gameParticipants);
    }

    @Override
    public GameEntryDTS getGameEntry(Long gameEntryId) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new EntityNotFoundException("GameEntry not found for id: " + gameEntryId));

        Game game = gameEntry.getGame();
        Group group = gameEntry.getGroup();

        List<GameParticipants> gameParticipants = gameParticipantsRepository.findByGameEntryId(gameEntryId);

        List<GameParticipantsDts> gameParticipantsDts = mapToGameParticipantDts(gameParticipants);

        boolean userEntityOwner = entityOwnershipChecker.isCurrentUserOwner(gameEntry.getCreatedBy());

        return new GameEntryDTS(gameEntry.getId(), gameEntry.getCreated(), gameEntry.getEntryTitle(), game.getGameTitle(), gameEntry.getDescription(), group.getGroupName(), gameParticipantsDts, userEntityOwner);
    }

    @Override
    @Transactional
    public void createGameEntry(CreateGameEntryRequest createGameEntryRequest) {
        Game game = gameRepository.findById(createGameEntryRequest.getGameId()).orElseThrow(EntityNotFoundException::new);
        Group group = groupRepository.findById(createGameEntryRequest.getGroupId()).orElseThrow(EntityNotFoundException::new);

        GameEntry gameEntryEntity = GameEntry.builder()
                .entryTitle(createGameEntryRequest.getEntryTitle())
                .description(createGameEntryRequest.getDescription())
                .game(game)
                .group(group)
                .created(LocalDateTime.now())
                .build();

        gameEntryRepository.save(gameEntryEntity);
    }

    @Override
    @Transactional
    public void editGameEntry(EditGameEntryRequest editGameEntryRequest) {
        GameEntry gameEntry = gameEntryRepository.findById(editGameEntryRequest.getGameEntryId()).orElseThrow(
                () -> new EntityNotFoundException("GameEntry could not be found with id: " + editGameEntryRequest.getGameEntryId()));

        validateGameEntryOwner(gameEntry);

        gameEntry.setEntryTitle(editGameEntryRequest.getEntryTitle());
        gameEntry.setDescription(editGameEntryRequest.getDescription());

        gameEntryRepository.save(gameEntry);
    }

    @Override
    public List<GameEntryWhichUserParticipated> findAllGameEntriesByUserByOrderCreatedDesc(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("User with userName " + userName + " doesnt exists"));

        List<GameParticipants> gameParticipants = gameParticipantsRepository.findGameEntriesByUser(user);
        if (gameParticipants.isEmpty()) {
            throw new IllegalArgumentException("Game entries for " + userName + " not found");
        }
        return gameParticipants.stream()
                .map(participant -> {
                    GameEntry gameEntry = participant.getGameEntry();
                    List<GameParticipants> gameParticipants1 = gameParticipantsRepository.findByGameEntry(gameEntry);
                    List<String> mappedParticipants = gameParticipants1.stream()
                            .map(participants1 -> participants1.getUser().getUserName())
                            .toList();

                    return new GameEntryWhichUserParticipated(
                            gameEntry.getId(),
                            gameEntry.getEntryTitle(),
                            gameEntry.getGroup().getGroupName(),
                            gameEntry.getGame().getGameTitle(),
                            mappedParticipants,
                            gameEntry.getCreated(),
                            gameEntry.getComments().size()
                    );
                })
                .toList();
    }

    private List<GameParticipantsDts> mapToGameParticipantDts(List<GameParticipants> gameParticipants) {
        return gameParticipants.stream().map(gameParticipant ->
                new GameParticipantsDts(userMapper.toDTS(gameParticipant.getUser()), gameParticipant.getPoint())).collect(toList());
    }

    private void validateGameEntryOwner(GameEntry gameEntry) {
        if (!entityOwnershipChecker.isCurrentUserOwner(gameEntry.getCreatedBy())) {
            throw new AccessDeniedException("Unauthorized operation.");
        }
    }

}



