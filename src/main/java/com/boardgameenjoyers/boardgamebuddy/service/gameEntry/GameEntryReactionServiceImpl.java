package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.controller.exception.ReactionAlreadyExistsException;
import com.boardgameenjoyers.boardgamebuddy.controller.exception.WrongReactionException;
import com.boardgameenjoyers.boardgamebuddy.dao.enums.ReactionType;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.*;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameEntryReactionServiceImpl implements GameEntryReactionService {

    private final GameEntryRepository gameEntryRepository;
    private final GameEntryReactionRepository gameEntryReactionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addReaction(Long gameEntryId, String username, ReactionType reactionType) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId).
                orElseThrow(() -> new EntityNotFoundException("GameEntry not found"));

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<GameEntryReaction> existingReaction = gameEntryReactionRepository.findByGameEntryIdAndUserUserName(gameEntryId, username);

        if (existingReaction.isEmpty()) {
            GameEntryReaction newReaction = new GameEntryReaction();
            newReaction.setGameEntry(gameEntry);
            newReaction.setReactionType(reactionType);
            newReaction.setUser(user);
            gameEntryReactionRepository.save(newReaction);
        } else {
            GameEntryReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == reactionType) {
                throw new ReactionAlreadyExistsException("You already reacted like that");
            } else {
                throw new IllegalArgumentException("You need to remove reaction first to add new one");
            }
        }
    }


    @Override
    public void removeReaction(Long gameEntryId, String username, ReactionType reactionType) {
        Optional<GameEntryReaction> existingReaction = gameEntryReactionRepository.findByGameEntryIdAndUserUserName(gameEntryId, username);
        if (existingReaction.isEmpty()) {
            throw new IllegalArgumentException("You haven't reacted yet to remove a reaction");
        } else {
            GameEntryReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == reactionType) {
                gameEntryReactionRepository.delete(reaction);
            } else {
                throw new WrongReactionException("Its not your reaction");
            }
        }
    }

    @Override
    public Map<ReactionType, Long> countReactions(Long gameEntryId) {
        List<ReactionTypeProjection> result = gameEntryReactionRepository.countByReactionType(gameEntryId);
        return result.stream().filter(entry -> entry.getType() != null)
                .collect(Collectors.toMap(ReactionTypeProjection::getType, ReactionTypeProjection::getCount));
    }

}
