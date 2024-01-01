package com.finalproject.mooc.model.responses;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressCourseNoSubject {

    private String teacher;
    private String courseCode;
    private String courseName;
    private CourseCategory courseCategory;
    private CourseLevel courseLevel;
    private com.finalproject.mooc.enums.TypePremium TypePremium;
    private Double coursePrice;
    private String courseAbout;
    private String courseFor;
    private String urlTele;
    private String progressBar;
}
