package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.gameComment.GameCommentRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.gameComment.GameComment;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.user.ApplicationUser;
import com.boardgameenjoyers.boardgamebuddy.service.gameEntry.GameEntryCommentDTS;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameCommentServiceImpl implements GameCommentService {
    private final GameCommentRepository gameCommentRepository;
    private final EntityOwnershipChecker entityOwnershipChecker;
    private final UserRepository userRepository;
    private final ApplicationUser applicationUser;
    private final GameRepository gameRepository;

    @Override
    public List<GameCommentDTS> getCommentsForGame(Long gameId) {
        List<GameComment> gameComments = gameCommentRepository.findByGameId(gameId);
        return gameComments.stream().map(this::getGameCommentDTS).sorted(Comparator.comparing(GameCommentDTS::getCreated)).toList();
    }


    @Override
    @Transactional
    public void addComment(Long gameId, String text) {
        UserDetails userDetails = applicationUser.getUserDetails();
        User user = userRepository.findByUserName((userDetails.getUsername())).orElseThrow(() -> new EntityNotFoundException("User is no logged in"));

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game With id " + gameId + " does not exist"));

        GameComment gameComment = new GameComment();
        gameComment.setText(text);
        gameComment.setGame(game);
        gameComment.setUser(user);

        gameCommentRepository.save(gameComment);
    }

    @Override
    public void deleteGameComment(Long id) {
        GameComment gameComment = gameCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find game comment with id: " + id));

        boolean currentUserOwner = entityOwnershipChecker.isCurrentUserOwner(gameComment.getUser().getUserName());

        if (currentUserOwner) {
            gameCommentRepository.delete(gameComment);
        } else {
            throw new AccessDeniedException("Access denied.");
        }
    }

    private GameCommentDTS getGameCommentDTS(GameComment gameComment) {
        User user = gameComment.getUser();
        GameEntryCommentDTS.UserData userData = new GameEntryCommentDTS.UserData(user.getId(), user.getUserName(), user.getEmail());
        return new
                GameCommentDTS(gameComment.getId(),
                gameComment.getText(),
                gameComment.getCreated(),
                userData,
                entityOwnershipChecker.isCurrentUserOwner(gameComment.getUser().getUserName()));
    }
}
