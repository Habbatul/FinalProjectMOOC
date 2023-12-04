package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.Course;
import com.finalproject.mooc.entity.SubjectProgress;
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

public interface CourseRepository extends JpaRepository<Course, String> {
//    @Query("SELECT DISTINCT c FROM Course c INNER JOIN c.subjects s WHERE UPPER(c.courseCategory) IN :categories")
//    Optional<Page<Course>> findCourseByCategory(@Param("categories") List<CourseCategory> category, Pageable pageable);

//    @Query("SELECT c FROM Course c WHERE UPPER(c.courseCategory) IN :categories")
//    Optional<Page<Course>> findCourseByCategory(@Param("categories") List<CourseCategory> category, Pageable pageable);

    /**
     * Untuk filter Kategory dan Level (dengan salah satu antara level atau kategory atau juga bisa filter gabungan keduanya)
     */
    @Query("SELECT c FROM Course c WHERE " +
            "( (c.courseCategory IN :category) OR :category IS NULL) AND " +
            "( (c.courseLevel IN :courseLevel) OR :courseLevel IS NULL) AND " +
            "( (c.TypePremium IN :typePremium) OR :typePremium IS NULL) AND " +
            "((LOWER(c.courseName) LIKE LOWER(CONCAT('%',:keyword, '%'))) OR :keyword IS NULL)")
    Optional<Page<Course>> findCourseByCategoryAndLevel(@Param("category") List<CourseCategory> category,
                                                        @Param("courseLevel") List<CourseLevel> courseLevel,
                                                        @Param("typePremium") List<TypePremium> typePremium,
                                                        @Param("keyword") String keyword,
                                                        Pageable pageable);

    @Query("SELECT c FROM Course c LEFT JOIN c.user WHERE c.user.username = :teacher AND" +
            "( (c.courseCategory IN :category) OR :category IS NULL) AND " +
            "( (c.courseLevel IN :courseLevel) OR :courseLevel IS NULL) AND " +
            "( (c.TypePremium IN :typePremium) OR :typePremium IS NULL) AND " +
            "((LOWER(c.courseName) LIKE LOWER(CONCAT('%',:keyword, '%'))) OR :keyword IS NULL)")
    Optional<Page<Course>> findCourseByAdmin(@Param("category") List<CourseCategory> category,
                                                        @Param("courseLevel") List<CourseLevel> courseLevel,
                                                        @Param("typePremium") List<TypePremium> typePremium,
                                                        @Param("keyword") String keyword,
                                                        @Param("teacher") String teacher,
                                                        Pageable pageable);

    @Query("SELECT c FROM Course c left JOIN c.subjects s WHERE c.idCourse = :courseCode")
    Optional<Course> findCourseJoinSubject(@Param("courseCode") String courseCode);

    @Query("SELECT COUNT(s) FROM Course c JOIN c.subjects s WHERE c.idCourse = :courseCode")
    Integer findTotalModule(@Param("courseCode") String courseCode);


    //untuk dashboard
    @Query("SELECT COUNT(c) from Course c LEFT JOIN c.user u WHERE u.username = :teacher")
    Integer countActiveClass(@Param("teacher")String teacher);

    @Query("SELECT COUNT(c) from Course c LEFT JOIN c.user u WHERE u.username = :teacher AND c.TypePremium = 'PREMIUM'")
    Integer countPremiumClass(@Param("teacher")String teacher);

//    @Query("SELECT c FROM Course c WHERE LOWER(c.courseName) LIKE LOWER(CONCAT('%',:keyword, '%'))")
//    Optional<Page<Course>> searchCourse(@Param("keyword") String keyword, Pageable pageable);

}
