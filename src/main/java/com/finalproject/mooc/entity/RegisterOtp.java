package com.finalproject.mooc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegisterOtp {

    @Id
    private String otp;
    private LocalDateTime otpGenerateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
