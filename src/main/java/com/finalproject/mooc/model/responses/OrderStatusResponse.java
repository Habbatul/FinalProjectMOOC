package com.finalproject.mooc.model.responses;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.PaidStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusResponse {

    private String buyerName;
    private CourseCategory courseCategory;
    private String coursePremiumName;
    private PaidStatus paidStatus;
    private String buyMethod;
    private LocalDateTime buyDate;
}
