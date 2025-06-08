package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper mapper;

    @Override
    public List<UserDTO> getDetailsAboutUsers(Optional<String> userSearch) {
        List<User> users;
        if (userSearch.isPresent()) {
            users = userRepository.findByUserNameContainingIgnoreCase(userSearch.get());
        } else {
            users = userRepository.findAll();
        }
        return users.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapper.toDTO(user);

    }

    @Override
    public User registerUser(UserDTO userDTO) {
        String hashedPassword = hashAndSaltPassword(userDTO.getPassword());

        User newUser = new User();
        newUser.setUserName(userDTO.getUserName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setCreated(LocalDateTime.now());

        return userRepository.save(newUser);
    }

    private String hashAndSaltPassword(String password) {
        return password;
    }

}
