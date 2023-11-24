package com.finalproject.mooc.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CoursePaginationResponse {
    List<CourseResponseNoSubject> courseResponseNoSubject;
    Integer productCurrentPage;
    Integer productTotalPage;
}
