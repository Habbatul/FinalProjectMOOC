package com.finalproject.mooc.entity;

import com.finalproject.mooc.enums.TypePremium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "subject_code")
    private String idSubject;
    private String title;
    private String url;
    private Integer chapter;

    private Integer sequence;

    @Enumerated(EnumType.STRING)
    private TypePremium TypePremium;
    @ManyToOne
    @JoinColumn(name = "course_code")
    private Course course;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SubjectProgress> subjectProgresses;
}
