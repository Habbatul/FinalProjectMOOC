package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.Course;
import com.finalproject.mooc.enums.CourseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
//    @Query("SELECT DISTINCT c FROM Course c INNER JOIN c.subjects s WHERE UPPER(c.courseCategory) IN :categories")
//    Optional<Page<Course>> findCourseByCategory(@Param("categories") List<CourseCategory> category, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE UPPER(c.courseCategory) IN :categories")
    Optional<Page<Course>> findCourseByCategory(@Param("categories") List<CourseCategory> category, Pageable pageable);

    @Query("SELECT c FROM Course c INNER JOIN c.subjects s WHERE c.idCourse like %:courseCode%")
    Optional<Course> findCourseJoinSubject(@Param("courseCode") String courseCode);

}
