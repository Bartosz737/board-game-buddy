package com.boardgameenjoyers.boardgamebuddy.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserWishlistRepository extends JpaRepository<UserWishlist, Long> {
    Optional<UserWishlist> findByUserUserNameAndGame_Id(String username, Long gameId);

    List<UserWishlist> findUserWishlistByUserUserName(String username);

    boolean existsByUserUserNameAndGame_Id(String username, Long gameId);
}
