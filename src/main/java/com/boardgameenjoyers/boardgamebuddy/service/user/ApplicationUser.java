package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface ApplicationUser {
    UserDetails getUserDetails();

    Set<UserRole> getRoles();
}
