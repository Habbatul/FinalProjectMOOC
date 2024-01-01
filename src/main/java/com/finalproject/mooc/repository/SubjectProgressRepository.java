package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.SubjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubjectProgressRepository extends JpaRepository<SubjectProgress, Integer> {

    @Query("SELECT COUNT(sp) FROM SubjectProgress sp WHERE sp.courseProgress.id = :courseProgressId")
    Long totalSubject(@Param("courseProgressId") Integer courseProgressId);

    @Query("SELECT COUNT(sp) * 100 FROM SubjectProgress sp WHERE sp.isDone = TRUE AND sp.courseProgress.id = :courseProgressId")
    Long totalPercentageSubjectIsDone(@Param("courseProgressId") Integer courseProgressId);


    @Modifying
    @Query("UPDATE SubjectProgress sp SET sp.isDone = true " +
            "WHERE sp IN (SELECT sp FROM SubjectProgress sp " +
            "JOIN sp.courseProgress cp " +
            "JOIN cp.user u " +
            "JOIN sp.subject s " +
            "WHERE u.username = :username " +
            "AND s.idSubject = :subjectCode)")
    void updateSubjectProgressIsDone(@Param("username") String username, @Param("subjectCode") String subjectCode);

}
