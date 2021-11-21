package com.example.auditor.service.transcript.parser.LetterGrade;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LetterGradeModifiedInstance {

    private LetterGrade letterGrade;
    private LetterGradeModifier letterGradeModifier;

    private Boolean affectsSemesterGPA;
    private Boolean affectsCGPA;
    private Boolean satisfiesDegreeRequirement;

    public LetterGradeModifiedInstance(LetterGrade letterGrade, LetterGradeModifier letterGradeModifier) {

        this.letterGrade = letterGrade;
        this.letterGradeModifier = letterGradeModifier;

        affectsSemesterGPA = letterGrade.getGroup().getAffectsSemesterGPA() && letterGradeModifier.getAffectsSemesterGPA();
        affectsCGPA = letterGrade.getGroup().getAffectsCGPA() && letterGradeModifier.getAffectsCGPA();
        satisfiesDegreeRequirement = letterGrade.getGroup().getSatisfiesDegreeRequirement() && letterGradeModifier.getSatisfiesDegreeRequirement();

    }


    public LetterGradeModifiedInstance(String letterGradeLiteral) throws LiteralNotMatchedException {

        this(
                LetterGrade.matchLiteral(letterGradeLiteral),
                LetterGradeModifier.matchLiteral(letterGradeLiteral)
        );
    }
}
