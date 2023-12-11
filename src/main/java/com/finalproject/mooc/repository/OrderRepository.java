package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.Course;
import com.finalproject.mooc.entity.Order;
import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.PaidStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.user.username = :username AND o.course.idCourse = :courseCode")
    Boolean findUserOrderExist(@Param("username") String username, @Param("courseCode") String courseCode);

    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.user.username = :username AND o.course.idCourse = :courseCode AND o.paid = 'SUDAH_BAYAR'")
    Boolean findIsPremiumPaid(@Param("username") String username, @Param("courseCode") String courseCode);

    @Query("SELECT o FROM  Order o WHERE o.user.username = :username ORDER BY o.orderDate DESC")
    Optional<List<Order>> findOrderHistory(@Param("username") String username);

    @Query("SELECT c FROM  Order o JOIN o.course c WHERE o.idOrder = :idOrder")
    Optional<Course> findCourseFromOrder(@Param("idOrder") Integer idOrder);

    @Query("SELECT o FROM Order o WHERE " +
            "( (o.course.courseCategory IN :category) OR :category IS NULL) AND " +
            "( (o.paid IN :paidStatus) OR :paidStatus IS NULL) AND " +
            "((LOWER(o.course.courseName) LIKE LOWER(CONCAT('%',:keyword, '%'))) OR :keyword IS NULL)")
    Optional<Page<Order>> findOrderByFilterSearchPagination(@Param("category") List<CourseCategory> category,
                                                            @Param("paidStatus") List<PaidStatus> paidStatus,
                                                            @Param("keyword") String keyword,
                                                            Pageable pageable);

    @Query("SELECT o FROM Order o LEFT JOIN o.course c WHERE c.user.username = :teacher AND" +
            "( (o.course.courseCategory IN :category) OR :category IS NULL) AND " +
            "( (o.paid IN :paidStatus) OR :paidStatus IS NULL) AND " +
            "((LOWER(o.course.courseName) LIKE LOWER(CONCAT('%',:keyword, '%'))) OR :keyword IS NULL)")
    Optional<Page<Order>> findOrderByTeacher(@Param("category") List<CourseCategory> category,
                                                            @Param("paidStatus") List<PaidStatus> paidStatus,
                                                            @Param("keyword") String keyword,
                                                            @Param("teacher") String teacher,
                                                            Pageable pageable);


    @Query("SELECT o FROM Order o WHERE o.user.username = :username AND o.course.idCourse = :courseId")
    Optional<Order> findOrderByUserIdAndCourseCode(@Param("username") String username,
                                                   @Param("courseId") String courseCode);
}
