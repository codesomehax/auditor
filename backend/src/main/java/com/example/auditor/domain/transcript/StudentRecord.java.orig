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
<<<<<<< HEAD
    private Integer creditsGraded;
    private Float gradePoints;
    private Float gpa;
=======
    private Integer currentSemester;
>>>>>>> 655e7a12068dcee2703ef07640217f488f47a40e

    @OneToMany(cascade = CascadeType.ALL)
    private List<StudentTerm> studentTerms;

}
