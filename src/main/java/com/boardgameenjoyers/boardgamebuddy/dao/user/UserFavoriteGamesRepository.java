package com.boardgameenjoyers.boardgamebuddy.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserFavoriteGamesRepository extends JpaRepository<UserFavoriteGames, Long> {

    List<UserFavoriteGames> findAllByUser_Id(Long userId);

    Optional<UserFavoriteGames> findByUserIdAndGameId(Long userId, Long gameId);

    boolean existsByUserIdAndGameId(Long userId, Long gameId);

    long countByUserId(Long userId);
}
