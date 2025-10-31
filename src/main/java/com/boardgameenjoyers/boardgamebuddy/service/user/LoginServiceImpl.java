package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.controller.exception.AuthenticationException;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import com.boardgameenjoyers.boardgamebuddy.service.request.utilRequest.RegisterAdminRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private ApplicationUser applicationUser;
    private AuthenticationManager authenticationManager;

    @Override
    public boolean doesUserExist(String userName, String email) {
        return userRepository.findByUserNameOrEmail(userName, email).isPresent();
    }

    @Override
    @Transactional
    public void register(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User newUser = new User
                (userDTO.getUserName(),
                        userDTO.getEmail(),
                        encodedPassword,
                        LocalDateTime.now(),
                        Collections.singleton(UserRole.USER));

        if (doesUserExist(userDTO.getUserName(), userDTO.getEmail())) {
            throw new EntityExistsException("User with this credentials already exists");
        }
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void registerAdmin(RegisterAdminRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User adminUser = new User(
                request.getUserName(),
                request.getEmail(),
                encodedPassword,
                LocalDateTime.now(),
                Set.of(UserRole.USER,
                        UserRole.ADMIN));

        if (doesUserExist(request.getUserName(), request.getEmail())) {
            throw new EntityExistsException("User with this credentials already exists");
        }

        userRepository.save(adminUser);
    }

    @Override
    public boolean login(UserCredentials userCredentials) {
        Optional<User> user = userRepository.findByUserName(userCredentials.getUsername());
        return user.isPresent() && passwordEncoder.matches(userCredentials.getPassword(), user.get().getPassword());
    }

    @Override
    public Set<String> getUserRoles() {
        return applicationUser.getUserDetails().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }


    @Override
    public void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("INVALID_CREDENTIALS", e);
        }
    }
}
