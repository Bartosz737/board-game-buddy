package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.get().getUserName(),
                user.get().getPassword(),
                getAuthorities(user.get().getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<UserRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }
}
