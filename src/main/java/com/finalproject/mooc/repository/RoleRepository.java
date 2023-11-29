package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.security.Roles;
import com.finalproject.mooc.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRoleName(ERole name);

}
