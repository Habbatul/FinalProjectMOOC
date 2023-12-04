package com.finalproject.mooc.controller;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.model.requests.CreateOrderRequest;
import com.finalproject.mooc.model.responses.*;
import com.finalproject.mooc.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Operation(summary = "Untuk menambahkan data order (paidStatus awal adalah BELUM_BAYAR")
    @PostMapping("/order")
    public ResponseEntity<WebResponse<OrderStatusResponse>> orderCourse(@RequestBody CreateOrderRequest orderRequest){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<OrderStatusResponse>builder()
                .data(orderService.orderCourse(username,orderRequest))
                .build());
    }

    @Operation(summary = "Menampilkan history order pelanggan")
    @GetMapping("/order/history")
    ResponseEntity<WebResponse<List<OrderHistoryResponse>>> showOrderHistory(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<List<OrderHistoryResponse>>builder()
                .data(orderService.showOrderHistory(username))
                .build());
    }


    @Operation(summary = "Menampilkan list Payment Status pada Admin dengan filter (Category dan Paid Status) serta fitur searching dan pagination")
    @GetMapping("admin/payment-status")
    public ResponseEntity<WebResponse<PaymentStatusPaginationResponse>> getPaymentStatusByFilterSearchPagination(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<CourseCategory> categories,
            @RequestParam(required = false) List<PaidStatus> status) {

        return ResponseEntity.ok(WebResponse.<PaymentStatusPaginationResponse>builder()
                .data(orderService.showPaymentStatusByFilterSearchPagination(page, categories, status, title))
                .build());
    }
}
