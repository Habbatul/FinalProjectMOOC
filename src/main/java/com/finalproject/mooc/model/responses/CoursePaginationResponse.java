package com.finalproject.mooc.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CoursePaginationResponse<T> {
    List<T> courseList;
    Integer productCurrentPage;
    Integer productTotalPage;
}
