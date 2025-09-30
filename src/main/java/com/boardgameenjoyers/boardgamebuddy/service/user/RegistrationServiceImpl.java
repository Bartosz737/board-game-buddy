package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
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

    @Override
    @Transactional
    public User register(UserDTO userDTO) {
        loginService.register(userDTO);
        User user = userRepository.findByUserName(userDTO.getUserName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userProfileService.createProfileForUser(user);
        return user;
    }
}
