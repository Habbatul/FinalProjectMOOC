package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.Subject;
import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.model.requests.CreateCourseRequest;
import com.finalproject.mooc.model.requests.CreateSubjectRequest;
import com.finalproject.mooc.model.responses.CoursePaginationResponse;
import com.finalproject.mooc.model.responses.CourseResponseNoSubject;
import com.finalproject.mooc.model.responses.CourseResponseWithSubject;
import com.finalproject.mooc.model.responses.SubjectResponse;

import java.util.List;

public interface CourseService {
    CourseResponseNoSubject createCourse(CreateCourseRequest courseRequest, String username);
    SubjectResponse createSubject(CreateSubjectRequest createSubjectRequest, String username, String courseCode);
    CoursePaginationResponse showCourse(Integer page, String username);
    CoursePaginationResponse showCourseByCategory(Integer page, List<CourseCategory> category, String username);
    CoursePaginationResponse showCourseBySearch(Integer page, String title, String username);
    CourseResponseWithSubject showDetailCourse(String courseCode);
}
