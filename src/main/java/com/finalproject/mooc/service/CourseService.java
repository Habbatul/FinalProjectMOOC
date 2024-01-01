package com.finalproject.mooc.service;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.requests.CreateCourseRequest;
import com.finalproject.mooc.model.requests.CreateSubjectRequest;
import com.finalproject.mooc.model.requests.UpdateCourseRequest;
import com.finalproject.mooc.model.requests.UpdateSubjectRequest;
import com.finalproject.mooc.model.responses.*;

import java.util.List;

public interface CourseService {

    CourseCreateResponse createCourse(CreateCourseRequest courseRequest, String username);

    CourseResponseNoSubject updateCourse(UpdateCourseRequest updateCourseRequest, String username, String courseCode);

    void deleteCourse(String courseCode, String username);

    SubjectDetail createSubject(CreateSubjectRequest createSubjectRequest, String username, String courseCode);

    SubjectDetail updateSubject(UpdateSubjectRequest updateSubjectRequest, String username, String courseCode, String subjectCode);

    void deleteSubject(String subjectCode, String username);

    CoursePaginationResponse<CourseResponseNoSubject> showCourseByCategoryOrLevelOrPremiumAndSearch(Integer page, List<CourseCategory> category, List<CourseLevel> courseLevel, List<TypePremium> typePremium, String keyword, String username);

    CourseResponseWithSubject showDetailCourse(String courseCode, String username);

    ManageCoursePaginationResponse showManageCourseByFilterSearchPagination(String username, Integer page, List<CourseCategory> category, List<CourseLevel> courseLevel, List<TypePremium> typePremium, String keyword);

}
