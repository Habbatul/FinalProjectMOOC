package com.finalproject.mooc.controller;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.responses.ManageCoursePaginationResponse;
import com.finalproject.mooc.model.responses.PaymentStatusPaginationResponse;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.service.CourseService;
import com.finalproject.mooc.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    OrderService orderService;
    @Autowired
    private CourseService courseService;

    @Operation(summary = "Menampilkan list Payment Status pada Admin dengan filter (Category dan Paid Status) serta fitur searching dan pagination")
    @GetMapping("admin/payment-status")
    public ResponseEntity<WebResponse<PaymentStatusPaginationResponse>> getPaymentStatusByFilterSearchPagination(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<CourseCategory> categories,
            @RequestParam(required = false) List<PaidStatus> status) {

        String teacher = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<PaymentStatusPaginationResponse>builder()
                .data(orderService.showPaymentStatusByFilterSearchPagination(teacher, page, categories, status, title))
                .build());
    }

    @Operation(summary = "Menampilkan list Manage Course pada Admin dengan filter (Category, Level, dan Premium) serta fitur searching dan pagination")
    @GetMapping("admin/manage-course")
    public ResponseEntity<WebResponse<ManageCoursePaginationResponse>> getManageCourseByFilterSearchPagination(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<CourseCategory> categories,
            @RequestParam(required = false) List<CourseLevel> courseLevel,
            @RequestParam(required = false) List<TypePremium> isPremium) {

        String teacher = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<ManageCoursePaginationResponse>builder()
                .data(courseService.showManageCourseByFilterSearchPagination(teacher, page, categories, courseLevel, isPremium, title))
                .build());
    }
}
