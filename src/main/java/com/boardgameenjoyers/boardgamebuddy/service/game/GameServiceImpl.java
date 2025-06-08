package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import com.boardgameenjoyers.boardgamebuddy.service.request.GamesPlayedPerMonth;
import com.boardgameenjoyers.boardgamebuddy.service.user.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ApplicationUser applicationUser;
    private final GameMapper gameMapper;
    private final GameEntryRepository gameEntryRepository;

    @Override
    public GameDTO getGameById(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Game with id: " + id + " not found"));
        return gameMapper.toDTO(game);
    }

    @Override
    public GamesPlayedPerMonth getGamesPlayedPerMonth(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with id " + gameId + " doesnt exists"));

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<GameEntry> gameEntriesWithGame = gameEntryRepository.findAllGameEntriesWithGame(game);
        if (gameEntriesWithGame.isEmpty()) {
            throw new IllegalArgumentException("Game entries with game " + gameId + " doesnt exists");
        }
        Long gamePlayed = gameRepository.countGamesInGameEntriesLastMonth(game, oneMonthAgo);
        return new GamesPlayedPerMonth(
                game.getId(),
                game.getGameTitle(),
                gamePlayed
        );
    }

    @Override
    @Transactional
    public GameDTO createNewGame(GameDTO gameDTO) {
        if (!applicationUser.getRoles().contains(UserRole.ADMIN)) {
            throw new AccessDeniedException("Unauthorized operation.");
        }
        Game newGame = new Game(gameDTO.getGameId(),
                gameDTO.getGameTitle(),
                gameDTO.getDescription(),
                gameDTO.getMinPlayers(),
                gameDTO.getMaxPLayers(),
                gameDTO.getAge(),
                gameDTO.getGameTime());
        Game savedGame = gameRepository.save(newGame);

        return gameMapper.toDTO(savedGame);
    }

    @Override
    public List<GameDTO> findAllGames() {
        return gameRepository.findAll().stream()
                .map(gameMapper::toDTO).collect(Collectors.toList());
    }
}


