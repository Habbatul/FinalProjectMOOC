package com.finalproject.mooc.service;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.responses.CoursePaginationResponse;
import com.finalproject.mooc.model.responses.ProgressCourseNoSubject;
import com.finalproject.mooc.model.responses.ProgressCourseResponse;

import java.util.List;

public interface CourseProgressService {

    ProgressCourseResponse showProgressCourseByUsernameAndCourseCode(String username, String courseCode);

    CoursePaginationResponse<ProgressCourseNoSubject> showProgressCourseListByUsername(String username, List<CourseCategory> categories,
                                                                                       List<CourseLevel> courseLevels,
                                                                                       List<TypePremium> typePremiums,
                                                                                       String keyword, Integer page);

    ProgressCourseNoSubject startCourse(String username, String courseCode);

    void editDoneCourse(String username, String subjectCode);

}
