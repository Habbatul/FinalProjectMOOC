package com.finalproject.mooc.service;

import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.model.responses.OrderHistoryResponse;
import com.finalproject.mooc.model.responses.OrderStatusResponse;

public interface OrderService {
    OrderStatusResponse showOrder(Integer page);
    OrderStatusResponse showOrderFiltered(Integer page, PaidStatus paidStatus);

    OrderHistoryResponse showOrderHistory();
}
