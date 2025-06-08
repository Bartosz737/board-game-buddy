package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserWishlist;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserWishlistRepository;
import com.boardgameenjoyers.boardgamebuddy.service.request.UserWishlistCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserWishlistServiceImpl implements UserWishlistService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserWishlistRepository userWishlistRepository;

    @Override
    @Transactional
    public void AddGameToWishlist(UserWishlistCreationRequest userWishlistCreationRequest) {
        User user = userRepository.findByUserName(getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Game game = gameRepository.findById(userWishlistCreationRequest.getGameId())
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        boolean existingWishlist = userWishlistRepository.existsByUserUserNameAndGame_Id(getCurrentUsername(), game.getId());

        if (existingWishlist) {
            throw new IllegalArgumentException("Game already added to wishlist");
        } else {
            UserWishlist userWishlist = new UserWishlist();
            userWishlist.setUser(user);
            userWishlist.setGame(game);

            userWishlistRepository.save(userWishlist);
        }
    }

    @Override
    public void removeGameFromWishlist(Long gameId) {

        Optional<UserWishlist> attemptToRemoveGameFromWishlist = userWishlistRepository.findByUserUserNameAndGame_Id(getCurrentUsername(), gameId);
        if (attemptToRemoveGameFromWishlist.isEmpty()) {
            throw new IllegalArgumentException("Game is not on wishlist");
        }
        userWishlistRepository.delete(attemptToRemoveGameFromWishlist.get());
    }

    @Override
    public List<UserWishlistDTO> getUserWishlist() {

        List<UserWishlist> userWishlist = userWishlistRepository.findUserWishlistByUserUserName(getCurrentUsername());

        return userWishlist.stream().map(wishlist -> UserWishlistDTO.builder()
                .gameId(wishlist.getGame().getId())
                .gameTitle(wishlist.getGame().getGameTitle()).build()).toList();
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
