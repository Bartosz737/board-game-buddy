package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.request.RegisterAdminRequest;

public interface RegistrationService {
    User register(UserDTO userDTO);

    User registerAdmin(RegisterAdminRequest request);


}
