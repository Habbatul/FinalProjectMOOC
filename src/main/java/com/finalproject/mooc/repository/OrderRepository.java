package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.user.username = :username AND o.course.idCourse = :courseCode")
    Boolean findUserOrderExist(@Param("username") String username, @Param("courseCode") String courseCode);

    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.user.username = :username AND o.course.idCourse = :courseCode AND o.paid = 'SUDAH_BAYAR'")
    Boolean findIsPremiumPaid(@Param("username") String username, @Param("courseCode") String courseCode);
}
