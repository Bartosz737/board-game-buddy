package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategory;
import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategoryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.*;
import com.boardgameenjoyers.boardgamebuddy.service.request.userFavoriteList.AddGameCategoryToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGameCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFavoriteGameCategoryServiceImpl implements UserFavoriteGameCategoryService {

    private final UserRepository userRepository;
    private final UserFavoriteGameCategoryRepository userFavoriteGameCategoryRepository;
    private final UserFavoriteGameCategoryMapper userFavoriteGameCategoryMapper;
    private final GameCategoryRepository gameCategoryRepository;

    @Override
    public void addGameCategoryAsFavorite(AddGameCategoryToUserFavoriteListRequest request) {
        Long currentUserId = getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + currentUserId));
        GameCategory gameCategory = gameCategoryRepository.findGameCategoryById(request.getGameCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Game category not found with id: " + request.getGameCategoryId()));

        userRepository.findAndLockById(currentUserId);
        long currentCount = userFavoriteGameCategoryRepository.countByUserId(currentUserId);
        if (currentCount >= 5) {
            throw new IllegalArgumentException("Favorite category list is full (5), remove some first before you add");
        }
        boolean gameCategoryExistsInList = userFavoriteGameCategoryRepository
                .existsByUserIdAndGameCategoryId(currentUserId, request.getGameCategoryId());
        if (gameCategoryExistsInList) {
            throw new IllegalArgumentException("Game category already in a list");
        }
        UserFavoriteGameCategory newUserFavoriteGameCategory = new UserFavoriteGameCategory();
        newUserFavoriteGameCategory.setUser(user);
        newUserFavoriteGameCategory.setGameCategory(gameCategory);
        userFavoriteGameCategoryRepository.save(newUserFavoriteGameCategory);
    }

    @Override
    public void removeGameCategoryFromUserFavoriteList(Long gameCategoryId) {
        Long currentUserId = getCurrentUserId();
        Optional<UserFavoriteGameCategory> attemptToRemoveGameCategoryFromUserFavoriteList = userFavoriteGameCategoryRepository
                .findByUserIdAndGameCategoryId(currentUserId, gameCategoryId);
        if (attemptToRemoveGameCategoryFromUserFavoriteList.isEmpty()) {
            throw new EntityNotFoundException("Game category not found with id: " + gameCategoryId);
        }
        userFavoriteGameCategoryRepository.delete(attemptToRemoveGameCategoryFromUserFavoriteList.get());
    }

    @Override
    public void createUserFavoriteGameCategoryList(User user) {
        Objects.requireNonNull(user, "User must exists first");
    }

    @Override
    public List<UserFavoriteGameCategoryResponse> getUserFavoriteGameCategoryList(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exists with id: " + userId));
        return userFavoriteGameCategoryRepository.findAllByUserId(userId).stream()
                .map(userFavoriteGameCategoryMapper::toResponse)
                .toList();
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
