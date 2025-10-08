package com.boardgameenjoyers.boardgamebuddy.util;

import com.boardgameenjoyers.boardgamebuddy.service.user.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityOwnershipChecker {

    private final ApplicationUser applicationUser;

    public boolean isCurrentUserOwner(String createdBy) {
        String currentUsername = applicationUser.getUserDetails().getUsername();
        if (currentUsername == null) {
            throw new IllegalStateException("Current user is not authenticated or username is null.");
        }
        return currentUsername.equals(createdBy);
    }

    public boolean isCurrentUserOwnerOfUserProfile(String username) {
        String currentUsername = applicationUser.getUserDetails().getUsername();
        if (currentUsername == null) {
            throw new IllegalStateException("Current user is not authenticated or username is null.");
        }
        return currentUsername.equals(username);
    }
}
