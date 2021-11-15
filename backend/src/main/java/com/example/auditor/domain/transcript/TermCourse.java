package com.example.auditor.domain.transcript;


import com.example.auditor.service.transcript.parser.LetterGrade.LetterGradeModifiedInstance;
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
    private String letterGradeLiteral;

    @Transient
    private LetterGradeModifiedInstance letterGradeModifiedInstance;


}
