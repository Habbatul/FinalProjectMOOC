package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmailAddress(String emailAddress);
    Boolean existsByEmailAddress(String email);
    Boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT r.roleName FROM User u JOIN u.roles r WHERE u.username = :username")
    Optional<Set<ERole>> findRoleNamesByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.userId = :userId")
    void updatePasswordByUserId(@Param("userId") Long userId, @Param("newPassword")String newPassword);
}
