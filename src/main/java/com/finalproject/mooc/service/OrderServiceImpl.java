package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.*;
import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.PaidStatus;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.requests.CreateOrderRequest;
import com.finalproject.mooc.model.responses.OrderHistoryResponse;
import com.finalproject.mooc.model.responses.OrderStatusResponse;
import com.finalproject.mooc.model.responses.PaymentStatusPaginationResponse;
import com.finalproject.mooc.model.responses.PaymentStatusResponse;
import com.finalproject.mooc.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CourseProgressRepository courseProgressRepository;
    @Autowired
    SubjectProgressRepository subjectProgressRepository;

    @Override
    public OrderStatusResponse showOrderFiltered(Integer page, List<CourseCategory> category, List<CourseLevel> courseLevel, List<TypePremium> typePremium, String keyword, String username) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderHistoryResponse> showOrderHistory(String username) {
        List<Order> orders = orderRepository.findOrderHistory(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order tidak ditemukan"));
        return toOrderHistoryResponse(orders);
    }

    @Transactional
    @Override
    public OrderStatusResponse orderCourse(String username, CreateOrderRequest orderRequest) {

        //validasi apabila pernah order
        if (orderRepository.findUserOrderExist(username, orderRequest.getCourseCode()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anda sudah order course ini sebelumnya");

        Course course = courseRepository.findById(orderRequest.getCourseCode()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course tidak ditemukan"));

        //validasi apabila course ternyata free
        if (course.getTypePremium().equals(TypePremium.FREE))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course ini gratis, anda hanya perlu memulainya");


        Order order = Order.builder()
                .orderDate(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .orderMethod(orderRequest.getOrderMethod())
                .paid(PaidStatus.BELUM_BAYAR)
                .course(course)
                .user(userRepository.findUserByUsername(username).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan")))
                .build();

        return toOrderStatusResponse(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderStatusResponse updatePaidStatus(String username, String courseCode) {
        Order order = orderRepository.findOrderByUserIdAndCourseCode(username, courseCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order tidak ditemukan"));
        order.setPaid(PaidStatus.SUDAH_BAYAR);
        orderRepository.save(order);


        //tambahkan ke progress course user bila paid status sudah_bayar
        CourseProgress userCourse = CourseProgress.builder()
                .user(userRepository.findUserByUsername(username).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "username tidak ditemukan")))
                .course(order.getCourse())
                .build();
//        userCourse.setSubjectProgresses(subjectToSubjectProgress(order.getCourse().getSubjects(), userCourse));
        courseProgressRepository.save(userCourse);
        subjectProgressRepository.saveAll(subjectToSubjectProgress(order.getCourse().getSubjects(), userCourse));


        return toOrderStatusResponse(orderRepository.save(order));
    }

    @Transactional
    @Override
    public PaymentStatusPaginationResponse showPaymentStatusByFilterSearchPagination(String username, Integer page, List<CourseCategory> category, List<PaidStatus> paidStatus, String keyword) {
        log.info("PaymentStatusPagination bejalan");
        page -= 1; //halaman asli dari index 0
        //sementara size nya 3
        Pageable halaman = PageRequest.of(page, 3);
        Page<Order> orderPage = orderRepository.findOrderByTeacher(category, paidStatus, keyword, username, halaman)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data tidak ditemukan"));

        if (orderPage.getContent().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data tidak ditemukan");

        return toPaymentStatusPaginationResponse(orderPage);
    }

    private OrderStatusResponse toOrderStatusResponse(Order order) {
        return OrderStatusResponse.builder()
                .coursePremiumName(order.getCourse().getCourseName())
                .buyerName(order.getUser().getUsername())
                .paidStatus(order.getPaid())
                .courseCategory(order.getCourse().getCourseCategory())
                .buyDate(order.getOrderDate())
                .buyMethod(order.getOrderMethod())
                .build();

    }

    private List<OrderHistoryResponse> toOrderHistoryResponse(List<Order> orders) {
        return orders.stream().map(
                order -> {
                    Course course = orderRepository.findCourseFromOrder(order.getIdOrder()).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ada kesalahan dalam menemukan course yang di order"));

                    return OrderHistoryResponse.builder()
                            .courseCode(course.getIdCourse())
                            .courseName(course.getCourseName())
                            .courseCategory(course.getCourseCategory())
                            .courseLevel(course.getCourseLevel())
                            .coursePrice(course.getCoursePrice())
                            .teacher(course.getUser().getUsername())
                            .numberOfModule(courseRepository.findTotalModule(course.getIdCourse()))
                            .isPaid(order.getPaid())
                            .build();
                }
        ).collect(Collectors.toList());
    }

    private PaymentStatusResponse toPaymentStatusResponse(Order order) {
        return PaymentStatusResponse.builder()
                .id(order.getIdOrder())
                .category(order.getCourse().getCourseCategory().name())
                .premiumCourse(order.getCourse().getCourseName())
                .status(order.getPaid().name())
                .paymentMethod(order.getOrderMethod())
                .paymentDate(order.getOrderDate())
                .build();
    }

    private PaymentStatusPaginationResponse toPaymentStatusPaginationResponse(Page<Order> orderPage) {
        List<Order> orderResponse = orderPage.getContent();

        List<PaymentStatusResponse> paymentStatusResponses = orderResponse.stream()
                .map(this::toPaymentStatusResponse)
                .collect(Collectors.toList());

        return PaymentStatusPaginationResponse.builder()
                .paymentStatusResponses(paymentStatusResponses)
                .productCurrentPage(orderPage.getNumber() + 1)
                .productTotalPage(orderPage.getTotalPages())
                .build();
    }

    //helper untuk konversi subjek ke subjectprogress
    private List<SubjectProgress> subjectToSubjectProgress(List<Subject> subjects, CourseProgress courseProgress){

        return subjects.stream()
                .map(subject -> SubjectProgress.builder()
                        .courseProgress(courseProgress)
                        .subject(subject)
                        .isDone(false)
                        .build())
                .collect(Collectors.toList());
    }

}