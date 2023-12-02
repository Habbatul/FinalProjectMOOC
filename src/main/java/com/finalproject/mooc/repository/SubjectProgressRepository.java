package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.SubjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SubjectProgressRepository extends JpaRepository<SubjectProgress, Integer> {

    @Query("SELECT COUNT(sp) FROM SubjectProgress sp WHERE sp.courseProgress.id = :courseProgressId")
    Long totalSubject(@Param("courseProgressId") Integer courseProgressId);

    @Query("SELECT COUNT(sp) * 100 FROM SubjectProgress sp WHERE sp.isDone = TRUE AND sp.courseProgress.id = :courseProgressId")
    Long totalPercentageSubjectIsDone(@Param("courseProgressId") Integer courseProgressId);

    @Transactional
    @Modifying
    @Query("UPDATE SubjectProgress sp SET sp.isDone = :isDone WHERE (sp.id = :subjectProgressId) AND (sp.courseProgress.id = :courseProgressId)")
    void updateSubjectProgressIsDone(@Param("isDone") Boolean isDone, @Param("subjectProgressId")Integer subjectProgressId);

}
