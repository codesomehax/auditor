package com.example.auditor.service.transcript.parser.LetterGrade;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LetterGradeGroup {

    A_THROUGH_D(true, true, true),
    F(true, true, false),
    W_UD(false, false, false),
    SD_P(false, false, true);

    private Boolean affectsSemesterGPA;
    private Boolean affectsCGPA;
    private Boolean satisfiesDegreeRequirement;

}
