package com.finalproject.mooc.model.responses;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.enums.TypePremium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class OrderStatusResponse {
    private String buyerName;
    private CourseCategory courseCategory;
    private String coursePremiumName;
    private PaidStatus paidStatus;
    private String buyMethod;
    private LocalDateTime buyDate;
}
