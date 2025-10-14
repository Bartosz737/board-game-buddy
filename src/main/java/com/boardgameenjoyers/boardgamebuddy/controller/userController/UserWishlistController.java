package com.boardgameenjoyers.boardgamebuddy.controller.userController;

import com.boardgameenjoyers.boardgamebuddy.service.request.UserWishlistCreationRequest;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserWishlistDTO;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserWishlistController {
    private final UserWishlistService userWishlistService;

    @PostMapping("user-wishlist")
    public ResponseEntity<List<UserWishlistCreationRequest>> addGameToWishlist(@RequestBody UserWishlistCreationRequest userWishlistCreationRequest) {
        userWishlistService.AddGameToWishlist(userWishlistCreationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("user-wishlist")
    public ResponseEntity<List<UserWishlistDTO>> getUserWishlist() {
        return ResponseEntity.ok(userWishlistService.getUserWishlist());
    }

    @DeleteMapping("user-wishlist/{gameId}")
    public ResponseEntity<?> removeGameFromWishlist(@PathVariable Long gameId) {
        userWishlistService.removeGameFromWishlist(gameId);
        return ResponseEntity.ok().build();
    }
}
