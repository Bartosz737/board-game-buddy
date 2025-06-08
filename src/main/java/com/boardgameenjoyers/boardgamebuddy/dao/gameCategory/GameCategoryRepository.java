package com.boardgameenjoyers.boardgamebuddy.dao.gameCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameCategoryRepository extends JpaRepository<GameCategory, Long> {
    Optional<GameCategory> findGameCategoryById(Long gameCategoryId);

    Optional<GameCategory> findByGameCategoryName(String gameCategoryName);

}
