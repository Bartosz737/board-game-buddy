package com.boardgameenjoyers.boardgamebuddy.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNameOrEmail(String userName, String email);

    Optional<User> findByUserName(String userName);

    Optional<User> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    User findAndLockById(@Param("id") Long id);

    @Query("select u.id from User u where u.userName = :userName")
    Optional<Long> findIdByUsername(@Param("userName") String userName);

    List<User> findByUserNameContainingIgnoreCase(String userName);
}
