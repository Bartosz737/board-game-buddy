package com.boardgameenjoyers.boardgamebuddy.dao.user;


import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGameCategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteGameCategoryRepository extends JpaRepository<UserFavoriteGameCategory, Long> {
    List<UserFavoriteGameCategory> findAllByUserId(Long userId);

    boolean existsByUserIdAndGameCategoryId(Long userId, Long gameCategoryId);

    long countByUserId(long userId);

    Optional<UserFavoriteGameCategory> findByUserIdAndGameCategoryId(Long userId, Long gameCategoryId);
}
