package com.boardgameenjoyers.boardgamebuddy.controller.userController;

import com.boardgameenjoyers.boardgamebuddy.service.request.userFavoriteList.AddGameCategoryToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGameCategoryResponse;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserFavoriteGameCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-favorite-game-category")
public class UserFavoriteGameCategoryController {

    private final UserFavoriteGameCategoryService userFavoriteGameCategoryService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserFavoriteGameCategoryResponse>> getUserFavoriteGameCategoryList(@PathVariable Long userId) {
        return ResponseEntity.ok(userFavoriteGameCategoryService.getUserFavoriteGameCategoryList(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<List<AddGameCategoryToUserFavoriteListRequest>>
    addGameCategoryToUserFavoriteList(@RequestBody AddGameCategoryToUserFavoriteListRequest addGameCategoryToUserFavoriteListRequest) {
        userFavoriteGameCategoryService.addGameCategoryAsFavorite(addGameCategoryToUserFavoriteListRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{gameCategoryId}")
    public ResponseEntity<?> removeGameCategoryFromUserFavoriteList(@PathVariable long gameCategoryId) {
        userFavoriteGameCategoryService.removeGameCategoryFromUserFavoriteList(gameCategoryId);
        return ResponseEntity.ok().build();
    }

}
