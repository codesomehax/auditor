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

    private Integer creditsEnrolled;
    private Integer creditsEarned;
    private Integer creditsGraded;
    private Float gradePoints;
    private Float gpa;

    @OneToMany(cascade = CascadeType.ALL)
    private List<StudentTerm> studentTerms;

}
