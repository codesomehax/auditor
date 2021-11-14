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

    /*private Integer creditsEnrolled;
    private Integer creditsEarned;
    private Integer creditsGraded;
    private Integer creditsGradedCumulative;
    private Integer creditsGradedCumulativeLocal;

    private double gradePoints;
    private double gradePointsCumulative;
    private double gradePointsCumulativeLocal;*/

    @OneToMany(cascade = CascadeType.ALL)
    private List<TermCourse> termCourses;

    private double termGpa;
}
