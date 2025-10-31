package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.security.JwtUtil;
import com.boardgameenjoyers.boardgamebuddy.service.user.RegistrationService;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserCredentials;
import com.boardgameenjoyers.boardgamebuddy.service.response.LoginResponseDTO;
import com.boardgameenjoyers.boardgamebuddy.service.user.LoginService;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserDTO;
import com.boardgameenjoyers.boardgamebuddy.service.request.utilRequest.RegisterAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        registrationService.register(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterAdminRequest request) {
        registrationService.registerAdmin(request);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserCredentials userCredentials) {
        final String jwt = jwtUtil.generateToken(userCredentials.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userCredentials.getUsername(), userCredentials.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO("Invalid username or password"));
        }

        return ResponseEntity.ok(new LoginResponseDTO(jwt));
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<String>> userRoles() {
        return ResponseEntity.ok(loginService.getUserRoles());
    }
}
