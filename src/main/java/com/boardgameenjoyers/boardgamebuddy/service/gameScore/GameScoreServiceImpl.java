package com.boardgameenjoyers.boardgamebuddy.service.gameScore;

import com.boardgameenjoyers.boardgamebuddy.controller.exception.AlreadyScoreExistsException;
import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameScore;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameScoreRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.service.request.GameScoreRequest;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserScoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameScoreServiceImpl implements GameScoreService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameScoreRepository gameScoreRepository;
    private final GameScoreMapper gameScoreMapper;

    @Override
    @Transactional
    public void addScore(Long gameId, int rating) {

        String currentUsername = getCurrentUsername();

        Game game = gameRepository.findById(gameId).
                orElseThrow(() -> new EntityNotFoundException("Game not found"));

        User user = userRepository.findByUserName(currentUsername).
                orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<GameScore> existingUserScore = gameScoreRepository.
                findByGameIdAndUserUserName(gameId, currentUsername);

        if (existingUserScore.isEmpty()) {
            GameScore newScore = existingUserScore.orElse(new GameScore());
            newScore.setGame(game);
            newScore.setUser(user);
            newScore.setRating(rating);
            gameScoreRepository.save(newScore);
        } else {
            GameScore score = existingUserScore.get();
            if (rating == score.getRating()) {
                throw new AlreadyScoreExistsException("You scored this game already");
            } else {
                throw new IllegalArgumentException("You need to remove your score first");
            }
        }
    }

    @Override
    public void removeScore(Long gameId, String username) {
        GameScore existingScore = gameScoreRepository.findByGameIdAndUserUserName(gameId, username)
                .orElseThrow(() -> new IllegalArgumentException("You haven't scored this game yet"));

        gameScoreRepository.delete(existingScore);
    }

    @Override
    public List<GameScoreDTO> getScoreForGame(Long gameId) {
        List<GameScore> score = gameScoreRepository.findAllByGameId(gameId);
        return score.stream().map(gameScoreMapper::toDTO).collect(Collectors.toList());

    }

    @Override
    public List<UserScoreDTO> getAllUserScores(String username) {
        return gameScoreRepository.findAllByUser_UserName(username)
                .stream().map(score -> new UserScoreDTO(
                        score.getGame().getId(),
                        score.getRating(),
                        score.getDateTime()
                )).collect(Collectors.toList());
    }

    @Override
    public Double getAverageScoreForGame(Long gameId) {
        return gameScoreRepository.findAverageScoreForGame(gameId);
    }

    @Override
    public List<TopGameScoreDTO> getTopFiveGames() {
        return gameScoreRepository.findTop5GamesByAverageRating(PageRequest.of(0, 5));
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
