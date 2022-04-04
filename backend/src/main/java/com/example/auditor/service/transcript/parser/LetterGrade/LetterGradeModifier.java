package com.example.auditor.service.transcript.parser.LetterGrade;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LetterGradeModifier {

    NONE(true, true, true, ""),
    SINGLESTAR(false, false, false, "*"),
    DOUBLESTAR(true, false, false, "**");

    private Boolean affectsSemesterGPA;
    private Boolean affectsCGPA;
    private Boolean satisfiesDegreeRequirement;
    private String literal;


    public static LetterGradeModifier matchLiteral(String letterGradeModifier) {

        if (letterGradeModifier.contains(DOUBLESTAR.literal)) {

            return DOUBLESTAR;
        }

        if (letterGradeModifier.contains(SINGLESTAR.literal)) {

            return SINGLESTAR;
        }

        return NONE;
    }
}
