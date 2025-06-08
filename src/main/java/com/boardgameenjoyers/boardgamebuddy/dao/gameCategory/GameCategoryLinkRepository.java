package com.boardgameenjoyers.boardgamebuddy.dao.gameCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameCategoryLinkRepository extends JpaRepository<GameCategoryLink, Long> {
    List<GameCategoryLink> findByGameCategoryId(Long gameCategoryId);
}
