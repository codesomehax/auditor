package com.example.auditor.service.transcript.parser.LetterGrade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum LetterGrade {

    A_MINUS(3.67, "A-", LetterGradeGroup.A_THROUGH_D),
    A(4.0, "A", LetterGradeGroup.A_THROUGH_D),
    B_PLUS(3.33, "B+", LetterGradeGroup.A_THROUGH_D),
    B_MINUS(2.67, "B-", LetterGradeGroup.A_THROUGH_D),
    B(3.0, "B", LetterGradeGroup.A_THROUGH_D),
    C_PLUS(2.33, "C+", LetterGradeGroup.A_THROUGH_D),
    C_MINUS(1.67, "C-", LetterGradeGroup.A_THROUGH_D),
    C(2.0, "C", LetterGradeGroup.A_THROUGH_D),
    D_PLUS(1.33, "D+", LetterGradeGroup.A_THROUGH_D),
    D(1.0, "D", LetterGradeGroup.A_THROUGH_D),
    
    F(0.0, "F", LetterGradeGroup.F),
    
    W(0.0, "W", LetterGradeGroup.W_UD),
    UD(0.0, "UD", LetterGradeGroup.W_UD),
    
    SD(0.0, "SD", LetterGradeGroup.SD_P),
    P(0.0, "P", LetterGradeGroup.SD_P);

    private Double gradePoint;
    private String literal;
    private LetterGradeGroup group;


    public static LetterGrade matchLiteral(String literal) throws LiteralNotMatchedException {

        for (LetterGrade letterGrade : LetterGrade.values()) {

            if (literal.startsWith(letterGrade.literal)) {

                return letterGrade;
            }
        }

        throw new LiteralNotMatchedException("LetterGradeLiteral \"" + literal + "\" not matched");
    }
    
}
