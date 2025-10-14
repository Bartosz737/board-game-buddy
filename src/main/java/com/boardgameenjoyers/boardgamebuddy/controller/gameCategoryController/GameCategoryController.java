package com.boardgameenjoyers.boardgamebuddy.controller.gameCategoryController;

import com.boardgameenjoyers.boardgamebuddy.service.gameCategory.GameCategoryService;
import com.boardgameenjoyers.boardgamebuddy.service.gameCategory.GameCategoryDTO;
import com.boardgameenjoyers.boardgamebuddy.service.request.CreateGameCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game-category")
@CrossOrigin
public class GameCategoryController {
    private final GameCategoryService gameCategoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<GameCategoryDTO>> getAllGameCategories() {
        List<GameCategoryDTO> gameCategories = gameCategoryService.getAllgamesCategory();
        return new ResponseEntity<>(gameCategories, HttpStatus.OK);
    }

    @GetMapping("{gameCategoryId}")
    public ResponseEntity<GameCategoryDTO> getGameCategory(@PathVariable Long gameCategoryId) {
        GameCategoryDTO gameCategoryDTO = gameCategoryService.getGameCategoryByName(gameCategoryId);
        return ResponseEntity.ok(gameCategoryDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<GameCategoryDTO> createGameCategory(@RequestBody CreateGameCategoryRequest createGameCategoryRequest) {
        GameCategoryDTO createdCategory = gameCategoryService.createGameCategory(createGameCategoryRequest);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
}