package com.boardgameenjoyers.boardgamebuddy.service.user;


import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserProfile;
import com.boardgameenjoyers.boardgamebuddy.service.event.GameParticipantAddedEvent;

public interface UserProfileService {

    UserProfileDTO getUserProfile(Long userId);

    UserProfile createProfileForUser(User user);

    void updateProfileForUser(Long userId, UserProfileDTO userProfileDTO);

    void incrementGamesPlayed(GameParticipantAddedEvent event);
}
