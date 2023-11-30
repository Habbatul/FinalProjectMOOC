package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.requests.CreateOrderRequest;
import com.finalproject.mooc.model.responses.OrderStatusResponse;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.service.CourseService;
import com.finalproject.mooc.service.OrderService;
import com.finalproject.mooc.service.media.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<WebResponse<OrderStatusResponse>> orderCourse(@RequestBody CreateOrderRequest orderRequest){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(WebResponse.<OrderStatusResponse>builder()
                .data(orderService.orderCourse(username,orderRequest))
                .build());
    }
}
