package com.finalproject.mooc.model.responses;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ProgressCourseResponse {
    private String teacher;
    private String courseCode;
    private String courseName;
    private CourseCategory courseCategory;
    private CourseLevel courseLevel;
    private TypePremium TypePremium;
    private Double coursePrice;
    private String courseAbout;
    private String courseFor;
    private String urlTele;
    private List<ProgressSubjectResponse> progressSubject;
    private String progressBar;
}
