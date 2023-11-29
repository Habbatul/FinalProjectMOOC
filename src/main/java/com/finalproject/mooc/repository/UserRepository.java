package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmailAddress(String emailAddress);
    Boolean existsByEmailAddress(String email);
    Boolean existsByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.userId = :userId")
    void updatePasswordByUserId(@Param("userId") Long userId, @Param("newPassword")String newPassword);
}
