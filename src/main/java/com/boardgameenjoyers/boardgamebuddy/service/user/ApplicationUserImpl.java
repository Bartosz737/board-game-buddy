package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ApplicationUserImpl implements ApplicationUser {

    @Override
    public UserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("User is not logged-in.");
        }
    }

    @Override
    public Set<UserRole> getRoles() {
        return getUserDetails().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).map(authority -> UserRole.valueOf(authority.replaceAll("ROLE_", "")))
                .collect(Collectors.toSet());
    }
}
