package com.example.auditor.domain.transcript;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transcript")
@Entity
@Builder
public class StudentRecord {

    @Id
    private Long id;

    private String name;
    private String schoolName;
    private String major;
    private String admissionSemester;

    private Double gpa;
    private Double gradePointsTotal;

    private Integer creditsEnrolledTotal;
    private Integer creditsEarnedTotal;
    private Integer creditsGradedTotal;

    private Integer currentSemester;

    @OneToMany(cascade = CascadeType.ALL)
    private List<StudentTerm> studentTerms;

}