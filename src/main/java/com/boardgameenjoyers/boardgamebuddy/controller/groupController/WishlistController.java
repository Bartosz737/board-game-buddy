package com.boardgameenjoyers.boardgamebuddy.controller.groupController;

import com.boardgameenjoyers.boardgamebuddy.service.group.GroupWishlistService;
import com.boardgameenjoyers.boardgamebuddy.service.group.GroupWishlistDTS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("group-wishlist")
@CrossOrigin
public class WishlistController {
    private final GroupWishlistService groupWishlistService;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupWishlistDTS>> getGroupWishlist(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupWishlistService.getWishlist(groupId));
    }

    @PostMapping("/{groupId}/{gameId}")
    public ResponseEntity<Void> addGameToGroupWishlist(@PathVariable Long groupId, @PathVariable Long gameId) {
        groupWishlistService.addGameToGroupWishlist(groupId, gameId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long id) {
        groupWishlistService.removeFromWishlist(id);
        return ResponseEntity.ok().build();
    }

}
