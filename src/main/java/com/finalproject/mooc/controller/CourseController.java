package com.finalproject.mooc.controller;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.model.responses.CoursePaginationResponse;
import com.finalproject.mooc.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


//sementara yak guys belum bikin Response DTO nya aku


@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

    //sementara
    @GetMapping("/course")
    public ResponseEntity<CoursePaginationResponse> getCoursesByCategory(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false) List<CourseCategory> categories,
            @RequestParam(required = false) String username) {


        return ResponseEntity.ok(courseService.showCourseByCategory(page, categories, username));
    }

}
