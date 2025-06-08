package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        Long userId = user.getId();
        String userName = user.getUserName();
        String userEmail = user.getEmail();
        String password = user.getPassword();
        LocalDateTime created = user.getCreated();
        return new UserDTO(userId, userName, userEmail, password, created);
    }

    public UserDTS toDTS(User user) {
        Long userId = user.getId();
        String userName = user.getUserName();
        String userEmail = user.getEmail();
        LocalDateTime created = user.getCreated();
        return new UserDTS(userId, userName, userEmail, created);
    }
}
