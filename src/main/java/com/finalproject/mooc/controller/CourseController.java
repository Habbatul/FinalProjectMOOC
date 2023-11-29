package com.finalproject.mooc.controller;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.requests.CreateCourseRequest;
import com.finalproject.mooc.model.requests.CreateSubjectRequest;
import com.finalproject.mooc.model.responses.*;
import com.finalproject.mooc.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//sementara yak guys belum bikin Response DTO nya aku


@RestController
@Slf4j
public class CourseController {
    @Autowired
    private CourseService courseService;

    //sementara

    @Operation(summary = "Menampilkan list course dengan filter (Category, Level, dan Premium) serta fitur searching dan pagination")
    @GetMapping("/course")
    public ResponseEntity<WebResponse<CoursePaginationResponse>> getCoursesByCategoryAndLevel(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<CourseCategory> categories,
            @RequestParam(required = false) List<CourseLevel> courseLevel,
            @RequestParam(required = false) List<TypePremium> isPremium,
            @RequestParam(required = false) String username) {

        return ResponseEntity.ok(WebResponse.<CoursePaginationResponse>builder()
                .data(courseService.showCourseByCategoryOrLevelOrPremiumAndSearch(page, categories, courseLevel, isPremium, title, username))
                .build());
    }

    @Operation(summary = "Menampilkan detail dari course beserta modul/subject sesuai id")
    @GetMapping("/course-detail")
    public ResponseEntity<WebResponse<CourseResponseWithSubject>> getShowCourseDetail(
            @RequestParam(value = "courseCode") String courseCode) {

        return ResponseEntity.ok(WebResponse.<CourseResponseWithSubject>builder()
                .data(courseService.showDetailCourse(courseCode))
                .build());
    }

    @Operation(summary = "Membuat course tanpa subject")
    @PostMapping("course/")
    public ResponseEntity<WebResponse<CourseCreateResponse>> createCourse(
            @RequestBody CreateCourseRequest courseRequest,
            @RequestParam String username) {

        return ResponseEntity.ok(WebResponse.<CourseCreateResponse>builder()
                .data(courseService.createCourse(courseRequest, username))
                .build()
        );
    }

    @Operation(summary = "Membuat subject, pastikan ada parameter course code nya")
    @PostMapping("subject/")
    public ResponseEntity<WebResponse<SubjectResponse>> createModule(
            @RequestBody CreateSubjectRequest createSubjectRequest,
            @RequestHeader String courseCode,
            @RequestParam String username) {

        return ResponseEntity.ok(WebResponse.<SubjectResponse>builder()
                .data(courseService.createSubject(createSubjectRequest, username, courseCode))
                .build()
        );
    }

//tak Gabungin gaes ternyata bisa pakek query jadi lebih simple gaperlu ita itu

//    @GetMapping("/course")
//    public ResponseEntity<CoursePaginationResponse> showCourse(
//            @RequestParam(required = false, defaultValue = "1") Integer page,
//            @RequestParam(required = false) String username) {
//
//        return ResponseEntity.ok(courseService.showCourse(page, username));
//    }
//
//    @GetMapping("/course-search")
//    public ResponseEntity<CoursePaginationResponse> showCourseBySearch(
//            @RequestParam(required = false, defaultValue = "1") Integer page,
//            @RequestParam String title,
//            @RequestParam(required = false) String username) {
//
//        return ResponseEntity.ok(courseService.showCourseBySearch(page, title, username));
//    }

}
