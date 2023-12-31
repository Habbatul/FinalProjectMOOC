package com.finalproject.mooc.model.requests;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCourseRequest {

    private String courseName;
    private CourseCategory courseCategory;
    private TypePremium TypePremium;
    private CourseLevel courseLevel;
    private Double coursePrice;
    private String courseAbout;
    private String courseFor;
    private String urlTele;
}
