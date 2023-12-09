package com.finalproject.mooc.controller;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.responses.CoursePaginationResponse;
import com.finalproject.mooc.model.responses.ProgressCourseNoSubject;
import com.finalproject.mooc.model.responses.ProgressCourseResponse;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.service.CourseProgressService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class KelasBerjalan {
    @Autowired
    CourseProgressService courseProgressService;

    @Operation(summary = "Menampilkan data kelas yang berjalan berdaasarkan courcode yang dipilih")
    @GetMapping("/course-progress")
    ResponseEntity<WebResponse<ProgressCourseResponse>> showKelasBerjalan(@RequestParam String courseCode){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<ProgressCourseResponse>builder()
                .data(courseProgressService.showProgressCourseByUsernameAndCourseCode(username, courseCode))
                .build());
    }

    @Operation(summary = "Menampilkan seluruh data kelas yang berjalan")
    @GetMapping("/course-progress/list")
    ResponseEntity<WebResponse<CoursePaginationResponse<ProgressCourseNoSubject>>> showKelasBerjalanList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<CourseCategory> categories,
            @RequestParam(required = false) List<CourseLevel> courseLevel,
            @RequestParam(required = false) List<TypePremium> isPremium){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<CoursePaginationResponse<ProgressCourseNoSubject>>builder()
                .data(courseProgressService.showProgressCourseListByUsername(username, categories, courseLevel, isPremium, title, page))
                .build());
    }

    @Operation(summary = "Menambahkan ke Kelas Saya (menambahkan course yang diambil untuk user yang login)")
    @PostMapping ("/course-progress/start-course")
    ResponseEntity<WebResponse<ProgressCourseNoSubject>> startCourse(@RequestParam String courseCode){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<ProgressCourseNoSubject>builder()
                .data(courseProgressService.startCourse(username, courseCode))
                .build());
    }

    @Operation(summary = "Mengubah status modul/subject pada course yang dipilih menjadi done")
    @PutMapping("/course-progress/subject-done")
    ResponseEntity<WebResponse<String>> subjectProgressDone(@RequestParam String subjectCode){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        courseProgressService.editDoneCourse(username, subjectCode);
        return ResponseEntity.ok(WebResponse.<String>builder()
                .data("pengubahan dilakukan")
                .build());
    }

}
