package com.finalproject.mooc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //untuk mendapatkan data course yang sedang dijalankan
    @ManyToOne
    @JoinColumn(name = "course_code")
    private Course course;

    //untuk user yang mengambil kelas
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "courseProgress", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SubjectProgress> subjectProgresses;
}
