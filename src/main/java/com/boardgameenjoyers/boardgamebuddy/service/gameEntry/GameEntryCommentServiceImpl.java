package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryCommentRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;

import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryComment;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.user.ApplicationUser;
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
public class GameEntryCommentServiceImpl implements GameEntryCommentService {

    private final ApplicationUser applicationUser;
    private final UserRepository userRepository;
    private final GameEntryCommentRepository gameEntryCommentRepository;
    private final EntityOwnershipChecker entityOwnershipChecker;
    private final GameEntryRepository gameEntryRepository;

    @Override
    public List<GameEntryCommentDTS> getCommentsForGameEntry(Long gameEntryId) {
        List<GameEntryComment> comments = gameEntryCommentRepository.findByGameEntryId(gameEntryId);

        return comments.stream().map(this::getGameEntryCommentDTS).sorted(Comparator.comparing(GameEntryCommentDTS::getCreated)).toList();
    }

    @Override
    @Transactional
    public void addComment(Long gameEntryId, String text) {
        UserDetails userDetails = applicationUser.getUserDetails();
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User is not logged-in"));

        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find GameEntry with id: " + gameEntryId));

        GameEntryComment comment = new GameEntryComment();
        comment.setText(text);
        comment.setGameEntry(gameEntry);
        comment.setUser(user);

        gameEntryCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        GameEntryComment comment = gameEntryCommentRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        boolean currentUserOwner = entityOwnershipChecker.isCurrentUserOwner(comment.getUser().getUserName());
        if (currentUserOwner) {
            gameEntryCommentRepository.delete(comment);
        } else {
            throw new AccessDeniedException("Access denied.");
        }
    }

    private GameEntryCommentDTS getGameEntryCommentDTS(GameEntryComment gameEntryComment) {
        User user = gameEntryComment.getUser();
        GameEntryCommentDTS.UserData userData = new GameEntryCommentDTS.UserData(user.getId(), user.getUserName(), user.getEmail());
        return new GameEntryCommentDTS(gameEntryComment.getId(), gameEntryComment.getText(), gameEntryComment.getCreated(), userData,
                entityOwnershipChecker.isCurrentUserOwner(gameEntryComment.getUser().getUserName()));
    }
}
