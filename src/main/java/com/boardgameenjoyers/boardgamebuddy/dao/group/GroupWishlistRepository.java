package com.boardgameenjoyers.boardgamebuddy.dao.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupWishlistRepository extends JpaRepository<GroupWishlist, Long> {
    List<GroupWishlist> findByGroupId(Long groupId);
}
