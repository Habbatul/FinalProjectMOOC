package com.finalproject.mooc.service;

import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.requests.CreateOrderRequest;
import com.finalproject.mooc.model.responses.ManageCoursePaginationResponse;
import com.finalproject.mooc.model.responses.OrderHistoryResponse;
import com.finalproject.mooc.model.responses.OrderStatusResponse;
import com.finalproject.mooc.model.responses.PaymentStatusPaginationResponse;

import java.util.List;

public interface OrderService {
    OrderStatusResponse showOrder(Integer page);
    OrderStatusResponse showOrderFiltered(Integer page, PaidStatus paidStatus);

    OrderHistoryResponse showOrderHistory();

    OrderStatusResponse orderCourse(String username, CreateOrderRequest orderRequest);

    PaymentStatusPaginationResponse showPaymentStatusByFilterSearchPagination(Integer page, List<CourseCategory> category, List<PaidStatus> paidStatus, String keyword);
}
