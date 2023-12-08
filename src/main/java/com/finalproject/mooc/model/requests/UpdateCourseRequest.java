package com.finalproject.mooc.model.requests;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@AllArgsConstructor
@Builder
public class UpdateCourseRequest {
    private String courseName;
    @NotBlank
    private CourseCategory courseCategory;
    @NotBlank
    private TypePremium typePremium;
    @NotBlank
    private CourseLevel courseLevel;
    private Double coursePrice;
    private String courseAbout;
    private String courseFor;
    private String urlTele;
}
