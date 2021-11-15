package com.example.auditor.domain.transcript;

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
@Table(name = "term")
@Entity
public class StudentTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    private Integer creditsEnrolledThisTerm;
    private Integer creditsEarnedThisTerm;
    private Integer creditsGradedThisTerm;
    private Integer creditsGradedCumulative;
    private Integer creditsGradedTotal;

    private Double gradePointsThisTerm;
    private Double gradePointsCumulative;
    private Double gradePointsTotal;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TermCourse> termCourses;

    private Double termGpa;
    private Double termCumulativeGpa;
}
