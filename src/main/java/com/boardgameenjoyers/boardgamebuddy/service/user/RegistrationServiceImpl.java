package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.service.request.RegisterAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final LoginService loginService;
    private final UserFavoriteGamesService userFavoriteGamesService;
    private final UserFavoriteGameCategoryService userFavoriteGameCategoryService;

    @Override
    @Transactional
    public User register(UserDTO userDTO) {
        loginService.register(userDTO);
        User user = userRepository.findByUserName(userDTO.getUserName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userProfileService.createProfileForUser(user);
        userFavoriteGamesService.createUserFavoriteGamesList(user);
        userFavoriteGameCategoryService.createUserFavoriteGameCategoryList(user);
        return user;
    }

    @Override
    public User registerAdmin(RegisterAdminRequest request) {
        loginService.registerAdmin(request);
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userProfileService.createProfileForUser(user);
        return user;
    }
}
