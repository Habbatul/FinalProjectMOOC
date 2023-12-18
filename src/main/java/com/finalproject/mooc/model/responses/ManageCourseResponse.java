package com.finalproject.mooc.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ManageCourseResponse {

    private String courseCode;

    private String category;

    private String courseName;

    private String courseType;

    private String level;

    private Double coursePrice;

}
