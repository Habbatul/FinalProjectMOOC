package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, String> {
    @Query("SELECT t FROM ResetPassword t where t.token = :token")
    Optional<ResetPassword> findToken(@Param("token") String token);

}
