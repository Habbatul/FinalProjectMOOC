package com.finalproject.mooc.service;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.model.requests.CreateOrderRequest;
import com.finalproject.mooc.model.requests.UpdateOrderRequest;
import com.finalproject.mooc.model.responses.OrderHistoryResponse;
import com.finalproject.mooc.model.responses.OrderStatusResponse;
import com.finalproject.mooc.model.responses.PaymentStatusPaginationResponse;

import java.util.List;

public interface OrderService {

    List<OrderHistoryResponse> showOrderHistory(String username);

    OrderStatusResponse orderCourse(String username, CreateOrderRequest orderRequest);

    OrderStatusResponse updatePaidStatus(String username, String courseCode, UpdateOrderRequest updateOrderRequest);

    PaymentStatusPaginationResponse showPaymentStatusByFilterSearchPagination(String username, Integer page, List<CourseCategory> category, List<PaidStatus> paidStatus, String keyword);
}
