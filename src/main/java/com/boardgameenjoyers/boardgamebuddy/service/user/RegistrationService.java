package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;

public interface RegistrationService {
    User register(UserDTO userDTO);
}
