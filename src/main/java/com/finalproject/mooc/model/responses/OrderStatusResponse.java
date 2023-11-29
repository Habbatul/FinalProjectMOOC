package com.finalproject.mooc.model.responses;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.enums.TypePremium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderStatusResponse {
    private PaidStatus paidStatus;
    private String courseCode;
    private String courseName;
    private String teacherName;
    private Integer modulCount;
    private CourseCategory courseCategory;
    private CourseLevel courseLevel;
    private Double price;
}
