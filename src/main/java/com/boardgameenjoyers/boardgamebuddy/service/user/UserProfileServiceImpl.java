package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserProfile;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserProfileRepository;
import com.boardgameenjoyers.boardgamebuddy.service.event.GameParticipantAddedEvent;
import com.boardgameenjoyers.boardgamebuddy.service.event.GameParticipantRemoveEvent;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final GameParticipantsRepository gameParticipantsRepository;
    private final UserProfileRepository userProfileRepository;
    private final EntityOwnershipChecker entityOwnershipChecker;

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
        Objects.requireNonNull(user, " User must exist");
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

        validateUserProfileOwner(userProfile);

        userProfile.setBio(userProfileDTO.getBio());
        userProfileRepository.save(userProfile);
    }

    @Override
    @EventListener
    public void incrementGamesPlayed(GameParticipantAddedEvent event) {
        UserProfile userProfile = userProfileRepository.findByUserId(event.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userProfile.setGamesPlayed(userProfile.getGamesPlayed() + 1);
        userProfileRepository.save(userProfile);
    }

    @Override
    @EventListener
    public void decrementGamesPlayed(GameParticipantRemoveEvent event) {
        UserProfile userProfile = userProfileRepository.findByUserId(event.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (userProfile.getGamesPlayed() > 0) {
            userProfile.setGamesPlayed(userProfile.getGamesPlayed() - 1);
        }
        userProfileRepository.save(userProfile);
    }

    private void validateUserProfileOwner(UserProfile userProfile) {
        if (!entityOwnershipChecker.isCurrentUserOwnerOfUserProfile(userProfile.getUser().getUserName())) {
            throw new AccessDeniedException("It is not your profile, you do not have permission to change anything");
        }

    }
}



