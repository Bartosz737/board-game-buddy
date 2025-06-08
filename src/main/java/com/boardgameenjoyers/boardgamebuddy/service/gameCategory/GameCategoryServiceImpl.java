package com.boardgameenjoyers.boardgamebuddy.service.gameCategory;

import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategoryLinkRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategoryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategory;
import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategoryLink;
import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import com.boardgameenjoyers.boardgamebuddy.service.user.ApplicationUser;
import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameToCategoryRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.CreateGameCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameCategoryServiceImpl implements GameCategoryService {
    private final GameCategoryRepository gameCategoryRepository;
    private final GameCategoryLinkRepository gameCategoryLinkRepository;
    private final ApplicationUser applicationUser;
    private final GameRepository gameRepository;

    @Override
    public List<GameCategoryDTO> getAllgamesCategory() {
        List<GameCategory> categories = gameCategoryRepository.findAll();
        return categories.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<GameCategoryLinkResponse> getAllGamesInCategory(Long gameCategoryId) {
        List<GameCategoryLink> participants = gameCategoryLinkRepository.findByGameCategoryId(gameCategoryId);

        return participants.stream()
                .map(participant -> new GameCategoryLinkResponse(participant.getGame().getId(), participant.getGame().getGameTitle()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GameCategoryDTO createGameCategory(CreateGameCategoryRequest createGameCategoryRequest) {
        if (!applicationUser.getRoles().contains(UserRole.ADMIN)) {
            throw new AccessDeniedException("Unauthorized operation.");
        }

        gameCategoryRepository.findByGameCategoryName(createGameCategoryRequest.getGameCategoryName())
                .ifPresent(existingCategory ->
                {
                    throw new IllegalArgumentException("gameCategory with name: " +
                            "" + createGameCategoryRequest.getGameCategoryName() + " already exists");
                });

        GameCategory newGameCategory = new GameCategory();
        newGameCategory.setGameCategoryName(createGameCategoryRequest.getGameCategoryName());
        newGameCategory.setGameCategoryDescription(createGameCategoryRequest.getGameCategoryDescription());

        GameCategory savedGameCategory = gameCategoryRepository.save(newGameCategory);

        return new GameCategoryDTO(
                savedGameCategory.getId(), savedGameCategory.getGameCategoryName(),
                savedGameCategory.getGameCategoryDescription()
        );
    }

    @Override
    public GameCategoryDTO getGameCategoryByName(Long gameCategoryId) {
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryById(gameCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Game category not found: " + gameCategoryId));

        return new GameCategoryDTO(
                gameCategory.getId(), gameCategory.getGameCategoryName(),
                gameCategory.getGameCategoryDescription()
        );
    }

    @Override
    @Transactional
    public GameCategoryDTO addGameToGameCategory(AddGameToCategoryRequest addGameToCategoryRequest) {
        if (!applicationUser.getRoles().contains(UserRole.ADMIN)) {
            throw new AccessDeniedException("Unauthorized operation.");
        }

        GameCategory gameCategory = gameCategoryRepository.findGameCategoryById(addGameToCategoryRequest.getGameCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Game category not found :" + addGameToCategoryRequest.getGameCategoryId()));

        Game game = gameRepository.findById(addGameToCategoryRequest.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Game not found :" + addGameToCategoryRequest.getGameId()));

        GameCategoryLink gameCategoryLink = new GameCategoryLink();
        gameCategoryLink.setGame(game);
        gameCategoryLink.setGameCategory(gameCategory);

        gameCategoryLinkRepository.save(gameCategoryLink);
        return convertToDTO(gameCategory);
    }

    @Override
    public GameCategory getGameCategoryById(Long gameCategoryId) {
        return gameCategoryRepository.findGameCategoryById(gameCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Game category not found: " + gameCategoryId));
    }

    private GameCategoryDTO convertToDTO(GameCategory category) {
        return new GameCategoryDTO(
                category.getId(), category.getGameCategoryName(),
                category.getGameCategoryDescription()
        );
    }
}
