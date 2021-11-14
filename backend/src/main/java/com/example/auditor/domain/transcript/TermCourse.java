package com.example.auditor.domain.transcript;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
@Entity
public class TermCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private Integer credits;
    private Double gradePoint;
    private String letterGrade;

    private Boolean satisfiesDegreeRequirement;
    private Boolean affectsGPA;
    private Boolean affectsCGPA;

}
