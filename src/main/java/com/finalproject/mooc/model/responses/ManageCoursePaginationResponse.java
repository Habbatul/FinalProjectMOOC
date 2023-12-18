package com.finalproject.mooc.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageCoursePaginationResponse {

    List<ManageCourseResponse> manageCourseResponse;
    Integer productCurrentPage;
    Integer productTotalPage;
}
