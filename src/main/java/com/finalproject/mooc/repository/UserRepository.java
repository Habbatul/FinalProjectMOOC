package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
