package com.finalproject.mooc.service;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.requests.CreateCourseRequest;
import com.finalproject.mooc.model.requests.CreateSubjectRequest;
import com.finalproject.mooc.model.responses.*;

import java.util.List;

public interface CourseService {
    CourseCreateResponse createCourse(CreateCourseRequest courseRequest, String username);
    SubjectResponse createSubject(CreateSubjectRequest createSubjectRequest, String username, String courseCode);
    CoursePaginationResponse showCourseByCategoryOrLevelOrPremiumAndSearch(Integer page, List<CourseCategory> category, List<CourseLevel> courseLevel, List<TypePremium> typePremium, String keyword, String username);
    CourseResponseWithSubject showDetailCourse(String courseCode);

//    CoursePaginationResponse showCourse(Integer page, String username);
//    CoursePaginationResponse showCourseBySearch(Integer page, String title, String username);
//    CoursePaginationResponse showCourseList(Integer page, List<CourseCategory> category, Integer page, String title, String username)
}
