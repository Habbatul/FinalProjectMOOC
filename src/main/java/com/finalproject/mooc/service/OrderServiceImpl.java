package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.Order;
import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.model.requests.CreateOrderRequest;
import com.finalproject.mooc.model.responses.OrderHistoryResponse;
import com.finalproject.mooc.model.responses.OrderStatusResponse;
import com.finalproject.mooc.repository.CourseRepository;
import com.finalproject.mooc.repository.OrderRepository;
import com.finalproject.mooc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    UserRepository userRepository;


    @Autowired
    CourseRepository courseRepository;

    @Autowired
    OrderRepository orderRepository;

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

    @Override
    public OrderStatusResponse orderCourse(String username, CreateOrderRequest orderRequest) {
        if(orderRepository.findUserOrderExist(username, orderRequest.getCourseCode()))
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anda sudah order course ini sebelumnya");

        Order order = Order.builder()
                .orderDate(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .orderMethod(orderRequest.getOrderMethod())
                .paid(PaidStatus.BELUM_BAYAR)
                .course(courseRepository.findById(orderRequest.getCourseCode()).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course tidak ditemukan")))
                .user(userRepository.findUserByUsername(username).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan")))
                .build();

        return toOrderStatusResponse(orderRepository.save(order));
    }

    private OrderStatusResponse toOrderStatusResponse(Order order){
        return OrderStatusResponse.builder()
                .coursePremiumName(order.getCourse().getCourseName())
                .buyerName(order.getUser().getUsername())
                .paidStatus(order.getPaid())
                .courseCategory(order.getCourse().getCourseCategory())
                .buyDate(order.getOrderDate())
                .buyMethod(order.getOrderMethod())
                .build();

    }
}
