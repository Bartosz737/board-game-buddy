package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getDetailsAboutUsers(Optional<String> userSearch);

    UserDTO getUserById(Long id);

    User registerUser(UserDTO userDTO);

}
