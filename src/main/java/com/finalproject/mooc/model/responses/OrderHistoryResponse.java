package com.finalproject.mooc.model.responses;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.PaidStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHistoryResponse {

    private String courseCode;
    private String courseName;
    private CourseCategory courseCategory;
    private CourseLevel courseLevel;
    private Double coursePrice;
    private String teacher;
    private Integer numberOfModule;
    private PaidStatus isPaid;
}