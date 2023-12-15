package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.requests.CreateOrderRequest;
import com.finalproject.mooc.model.requests.UpdateOrderRequest;
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

    @Operation(summary = "mengubah paid status (paidStatus awal adalah BELUM_BAYAR menjadi SUDAH_BAYAR")
    @PostMapping("/order-updatePaidStatus")
    public ResponseEntity<WebResponse<OrderStatusResponse>> updatePaidStatus(
            @RequestBody UpdateOrderRequest updateOrderRequest,
            @RequestParam String courseCode){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<OrderStatusResponse>builder()
                        .data(orderService.updatePaidStatus(username, courseCode, updateOrderRequest))
                        .build());
    }
}
