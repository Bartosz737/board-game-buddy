package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserProfile;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserProfileRepository;
import com.boardgameenjoyers.boardgamebuddy.service.event.GameParticipantAddedEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final GameParticipantsRepository gameParticipantsRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        long gamesPlayed = gameParticipantsRepository.countByUserProfileUserId(userId);

        return new UserProfileDTO(
                userProfile.getUser().getUserName(),
                userProfile.getUser().getEmail(),
                userProfile.getBio(),
                gamesPlayed
        );
    }

    @Override
    public UserProfile createProfileForUser(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setBio("");
        userProfile.setGamesPlayed(0);
        return userProfileRepository.save(userProfile);
    }

    @Override
    public void updateProfileForUser(Long userId, UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userProfile.setBio(userProfileDTO.getBio());
        userProfileRepository.save(userProfile);
    }

    @Override
    @TransactionalEventListener
    public void incrementGamesPlayed(GameParticipantAddedEvent event) {
        UserProfile userProfile = userProfileRepository.findByUserId(event.UserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userProfile.setGamesPlayed(userProfile.getGamesPlayed() + 1);
        userProfileRepository.save(userProfile);
    }
}



