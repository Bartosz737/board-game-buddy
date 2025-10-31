package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.service.request.utilRequest.RegisterAdminRequest;

import java.util.Set;

public interface LoginService {

    boolean doesUserExist(String userName, String email);

    void register(UserDTO userDTO);

    void registerAdmin(RegisterAdminRequest request);

    boolean login(UserCredentials userCredentials);

    Set<String> getUserRoles();

    void authenticate(String username, String password);
}
