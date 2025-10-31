package com.boardgameenjoyers.boardgamebuddy.controller.gameCategoryController;

import com.boardgameenjoyers.boardgamebuddy.service.gameCategory.GameCategoryService;
import com.boardgameenjoyers.boardgamebuddy.service.gameCategory.GameCategoryDTO;
import com.boardgameenjoyers.boardgamebuddy.service.request.userFavoriteList.AddGameToCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class GameCategoryLinkController {
    private final GameCategoryService gameCategoryService;

    @PostMapping("/gameCategoryLink/{gameCategoryId}")
    public ResponseEntity<GameCategoryDTO> addGameToCategory(@RequestBody AddGameToCategoryRequest request) {
        GameCategoryDTO gameCategoryDTO = gameCategoryService.addGameToGameCategory(request);
        return ResponseEntity.ok(gameCategoryDTO);
    }
}
