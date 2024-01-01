package com.finalproject.mooc.controller;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.requests.CreateCourseRequest;
import com.finalproject.mooc.model.requests.CreateSubjectRequest;
import com.finalproject.mooc.model.requests.UpdateCourseRequest;
import com.finalproject.mooc.model.requests.UpdateSubjectRequest;
import com.finalproject.mooc.model.responses.*;
import com.finalproject.mooc.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Menampilkan list course dengan filter (Category, Level, dan Premium) serta fitur searching dan pagination")
    @GetMapping("/course")
    public ResponseEntity<WebResponse<CoursePaginationResponse<CourseResponseNoSubject>>> getCoursesByCategoryAndLevel(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<CourseCategory> categories,
            @RequestParam(required = false) List<CourseLevel> courseLevel,
            @RequestParam(required = false) List<TypePremium> isPremium) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<CoursePaginationResponse<CourseResponseNoSubject>>builder()
                .data(courseService.showCourseByCategoryOrLevelOrPremiumAndSearch(page, categories, courseLevel, isPremium, title, username))
                .build());
    }

    @Operation(summary = "Menampilkan detail dari course beserta modul/subject sesuai id")
    @GetMapping("/course-detail")
    public ResponseEntity<WebResponse<CourseResponseWithSubject>> getShowCourseDetail(
            @RequestParam(value = "courseCode") String courseCode) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("menjalankan courseDetail dengan username : {}", username);
        return ResponseEntity.ok(WebResponse.<CourseResponseWithSubject>builder()
                .data(courseService.showDetailCourse(courseCode, username))
                .build());
    }

    @Operation(summary = "Membuat course tanpa subject")
    @PostMapping("course")
    public ResponseEntity<WebResponse<CourseCreateResponse>> createCourse(
            @RequestBody CreateCourseRequest courseRequest) {

        //ambil nama dari Authorization Bearer
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(WebResponse.<CourseCreateResponse>builder()
                .data(courseService.createCourse(courseRequest, username))
                .build()
        );
    }

    @Operation(summary = "Edit course tanpa subject, pastikan ada parameter course code nya")
    @PutMapping("course")
    public ResponseEntity<WebResponse<CourseResponseNoSubject>> updateCourse(
            @RequestBody UpdateCourseRequest updateCourseRequest,
            @RequestParam String courseCode) {

        //ambil nama dari Authorization Bearer
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(WebResponse.<CourseResponseNoSubject>builder()
                .data(courseService.updateCourse(updateCourseRequest, username ,courseCode))
                .build()
        );
    }

    @Operation(summary = "Delete course with course code")
    @DeleteMapping("course/{courseCode}")
    public ResponseEntity<WebResponse<String>> deleteCourse(@PathVariable String courseCode) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        courseService.deleteCourse(courseCode, username);
        return ResponseEntity.ok().body(WebResponse.<String>builder().data("OK").build());
    }

    @Operation(summary = "Membuat subject, pastikan ada parameter course code nya")
    @PostMapping("subject")
    public ResponseEntity<WebResponse<SubjectDetail>> createModule(@RequestBody CreateSubjectRequest createSubjectRequest,
                                                                   @RequestParam String courseCode) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(WebResponse.<SubjectDetail>builder()
                .data(courseService.createSubject(createSubjectRequest, username, courseCode))
                .build()
        );
    }

    @Operation(summary = "Edit subject or module with course code, pastikan ada parameter course code nya and subject code")
    @PutMapping("subject")
    public ResponseEntity<WebResponse<SubjectDetail>> updateModule(
            @RequestParam String subjectCode,
            @RequestBody UpdateSubjectRequest updateSubjectRequest,
            @RequestParam(required = false) String courseCode) {

        //ambil nama dari Authorization Bearer
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(WebResponse.<SubjectDetail>builder()
                .data(courseService.updateSubject(updateSubjectRequest, username , courseCode, subjectCode))
                .build()
        );
    }

    @Operation(summary = "Delete subject with subject code")
    @DeleteMapping("subject/{subjectCode}")
    public ResponseEntity<WebResponse<String>> deleteSubject(@PathVariable String subjectCode) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        courseService.deleteSubject(subjectCode, username);
        return ResponseEntity.ok().body(WebResponse.<String>builder().data("OK").build());
    }

}
