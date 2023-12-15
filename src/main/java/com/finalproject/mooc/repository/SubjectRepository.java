package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.Course;
import com.finalproject.mooc.entity.Subject;
import com.finalproject.mooc.enums.TypePremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SubjectRepository extends JpaRepository<Subject, String> {
    @Transactional
    @Modifying
    @Query("UPDATE Subject s " +
            "SET s.TypePremium=:TypePremium, s.chapter=:chapter, s.sequence=:sequence, s.title=:title, s.url=:url, s.course=:courseCode " +
            "WHERE s.idSubject=:idSubject")
    void updateIdSubject(@Param("TypePremium") TypePremium typePremium, @Param("chapter") String chapter, @Param("sequence") Integer sequence, @Param("title") String title, @Param("url") String url, @Param("courseCode") Course courseCode, @Param("idSubject") String idSubject);

    @Query("SELECT COUNT(s) > 0 FROM Subject s WHERE s.sequence = :sequence AND s.course.idCourse = :courseCode")
    Boolean sequenceIsExistInCourse(@Param("sequence") Integer sequence, @Param("courseCode")String courseCode);

}
