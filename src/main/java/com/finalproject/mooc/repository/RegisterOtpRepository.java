package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.RegisterOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RegisterOtpRepository extends JpaRepository<RegisterOtp, String> {

    @Transactional
    @Modifying
    @Query("UPDATE RegisterOtp o SET o.otp = :#{#otpNew.otp}, o.otpGenerateTime = :#{#otpNew.otpGenerateTime} WHERE o.otp = :otpOld")
    void updatePasswordByOtp(@Param("otpOld") String otpOld, @Param("otpNew")RegisterOtp otpNew);

    @Query("SELECT otp.otp FROM RegisterOtp otp WHERE otp.user.emailAddress = :emailAddress")
    String getOtpOld(@Param("emailAddress")String emailAddress);

}
