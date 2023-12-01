package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.RegisterOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegisterOtpRepository extends JpaRepository<RegisterOtp, String> {
}
