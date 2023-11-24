package com.finalproject.mooc.service;

import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.model.responses.OrderHistoryResponse;
import com.finalproject.mooc.model.responses.OrderStatusResponse;

public class OrderServiceImpl implements OrderService{
    @Override
    public OrderStatusResponse showOrder(Integer page) {
        return null;
    }

    @Override
    public OrderStatusResponse showOrderFiltered(Integer page, PaidStatus paidStatus) {
        return null;
    }

    @Override
    public OrderHistoryResponse showOrderHistory() {
        return null;
    }
}
