package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.Course;
import com.finalproject.mooc.entity.CourseProgress;
import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CourseProgressRepository extends JpaRepository<CourseProgress, Integer> {
    @Query("SELECT cp FROM CourseProgress cp LEFT JOIN cp.user WHERE (cp.user.username = :username) AND cp.course.idCourse = :courseCode")
    Optional<CourseProgress> showCourseProgress(@Param("username")String username, @Param("courseCode")String courseCode);

    @Query("SELECT cp FROM CourseProgress cp WHERE cp.course.idCourse = :courseCode")
    Optional<List<CourseProgress>> showCourseProgressByCourseCode(@Param("courseCode")String courseCode);

    @Query("SELECT cp FROM CourseProgress cp LEFT JOIN cp.user WHERE (cp.user.username = :username) OR" +
            "( (cp.course.courseCategory IN :category) OR :category IS NULL) AND " +
            "( (cp.course.courseLevel IN :courseLevel) OR :courseLevel IS NULL) AND " +
            "( (cp.course.TypePremium IN :typePremium) OR :typePremium IS NULL) AND " +
            "((LOWER(cp.course.courseName) LIKE LOWER(CONCAT('%',:keyword, '%'))) OR :keyword IS NULL)")
    Optional<Page<CourseProgress>> showCourseProgressPagination(@Param("username")String username,
                                                                @Param("category") List<CourseCategory> category,
                                                                @Param("courseLevel") List<CourseLevel> courseLevel,
                                                                @Param("typePremium") List<TypePremium> typePremium,
                                                                @Param("keyword") String keyword,
                                                                Pageable pageable);

    @Query("SELECT COUNT(cp) > 0 FROM CourseProgress cp WHERE cp.user = :user AND cp.course = :course")
    Boolean findCourseProgressExist(@Param("user") User user, @Param("course") Course course);
}
