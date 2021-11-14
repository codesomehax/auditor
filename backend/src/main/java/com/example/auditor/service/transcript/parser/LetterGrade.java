package com.example.auditor.service.transcript.parser;


public enum LetterGrade {
    
    
    A(4.0, "A", LetterGradeGroup.A_THROUGH_D),
    A_MINUS(3.67, "A-", LetterGradeGroup.A_THROUGH_D),
    B_PLUS(3.33, "B+", LetterGradeGroup.A_THROUGH_D),
    B(3.0, "B", LetterGradeGroup.A_THROUGH_D),
    B_MINUS(2.67, "B-", LetterGradeGroup.A_THROUGH_D),
    C_PLUS(2.33, "C+", LetterGradeGroup.A_THROUGH_D),
    C(2.0, "C", LetterGradeGroup.A_THROUGH_D),
    C_MINUS(1.67, "C-", LetterGradeGroup.A_THROUGH_D),
    D_PLUS(1.33, "D+", LetterGradeGroup.A_THROUGH_D),
    D(1.0, "D", LetterGradeGroup.A_THROUGH_D),
    
    F(0.0, "F", LetterGradeGroup.F),
    
    W(0.0, "W", LetterGradeGroup.W_UD_SINGLESTAR),
    UD(0.0, "UD", LetterGradeGroup.W_UD_SINGLESTAR),
    SINGLE_STAR(0.0, "*", LetterGradeGroup.W_UD_SINGLESTAR),
    
    SD(0.0, "SD", LetterGradeGroup.SD_P),
    P(0.0, "P", LetterGradeGroup.SD_P),
    
    DOUBLE_STAR(0.0, "**", LetterGradeGroup.DOUBLESTAR);

    private Double gradePoint;
    private String literal;
    private LetterGradeGroup letterGradeGroup;
    

    private LetterGrade(Double gradePoint, String literal, LetterGradeGroup letterGradeGroup) {

        this.gradePoint = gradePoint;
        this.literal = literal;
        this.letterGradeGroup = letterGradeGroup;
        
    }

    public Double getGradePoint() {
        return gradePoint;
    }

    public String getLiteral() {
        return literal;
    }

    public LetterGradeGroup getLetterGradeGroup() {
        return letterGradeGroup;
    }

    public static LetterGrade match(String literal) throws LiteralNotMatchedException {

        if (literal.contains(DOUBLE_STAR.literal)) {

            return DOUBLE_STAR;
        }

        if (literal.contains(SINGLE_STAR.literal)) {

            return SINGLE_STAR;
        }

        for (LetterGrade letterGrade : LetterGrade.values()) {

            if (literal.equals(letterGrade.literal)) {

                return letterGrade;
            }
        }

        throw new LiteralNotMatchedException("Letter not matched");
    }
    
}
