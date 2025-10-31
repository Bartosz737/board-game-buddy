package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserFavoriteGames;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserFavoriteGamesRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.service.request.userFavoriteList.AddGameToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGamesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFavoriteGamesServiceImpl implements UserFavoriteGamesService {
    private final UserFavoriteGamesRepository userFavoriteGamesRepository;
    private final UserFavoriteGamesMapper userFavoriteGamesMapper;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Override
    public List<UserFavoriteGamesResponse> getUserFavoriteGames(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        return userFavoriteGamesRepository.findAllByUser_Id(userId)
                .stream()
                .map(userFavoriteGamesMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void addGameToUserFavoriteList(AddGameToUserFavoriteListRequest addGameToUserFavoriteListRequest) {
        Long currentUserId = getCurrentUserId();
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + currentUserId));
        Game game = gameRepository.findById(addGameToUserFavoriteListRequest.getGameId())
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + addGameToUserFavoriteListRequest.getGameId()));

        userRepository.findAndLockById(currentUserId);

        long currentCount = userFavoriteGamesRepository.countByUserId(currentUserId);
        if (currentCount >= 5) {
            throw new IllegalArgumentException("Favorite games list is full (5), remove some first before you add");
        }

        boolean gameExistsInList = userFavoriteGamesRepository.existsByUserIdAndGameId(currentUserId, addGameToUserFavoriteListRequest.getGameId());
        if (gameExistsInList) {
            throw new IllegalArgumentException("Game already in a list");
        }

        UserFavoriteGames userNewFavoriteGame = new UserFavoriteGames();
        userNewFavoriteGame.setUser(user);
        userNewFavoriteGame.setGame(game);
        userFavoriteGamesRepository.save(userNewFavoriteGame);

    }

    @Override
    public void removeGameFromUserFavoriteList(Long gameId) {
        Long currentUserId = getCurrentUserId();
        Optional<UserFavoriteGames> attemptToRemoveGameFromUserFavoriteList = userFavoriteGamesRepository.findByUserIdAndGameId(currentUserId, gameId);
        if (attemptToRemoveGameFromUserFavoriteList.isEmpty()) {
            throw new IllegalArgumentException("Game is not on a list");
        }
        userFavoriteGamesRepository.delete(attemptToRemoveGameFromUserFavoriteList.get());
    }


    @Override
    public void createUserFavoriteGamesList(User user) {
        Objects.requireNonNull(user, "user must exists first");
    }

    protected Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            try {
                throw new AccessDeniedException("Not authenticated");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
        String username = auth.getName();
        return userRepository.findIdByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found username: " + username));
    }
}
