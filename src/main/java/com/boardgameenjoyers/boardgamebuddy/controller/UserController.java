package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.service.user.UserService;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController implements Serializable {
    private UserService userService;

    @GetMapping("/all")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUserDetails(@RequestParam(required = false) String username) {
        List<UserDTO> users = userService.getDetailsAboutUsers(Optional.ofNullable(username));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}

