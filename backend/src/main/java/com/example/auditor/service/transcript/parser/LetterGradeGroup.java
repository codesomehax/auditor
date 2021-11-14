package com.example.auditor.service.transcript.parser;

public enum LetterGradeGroup {

    A_THROUGH_D(true, true, true),
    F(true, true, false),
    W_UD_SINGLESTAR(false, false, false),
    SD_P(false, false, true),
    DOUBLESTAR(true, false, true);

    private Boolean affectsGPA;
    private Boolean affectsCGPA;
    private Boolean satisfiesDegreeRequirement;

    private LetterGradeGroup(Boolean affectsGPA, Boolean affectsCGPA, Boolean satisfiesDegreeRequirement) {

        this.affectsGPA = affectsGPA;
        this.affectsCGPA = affectsCGPA;
        this.satisfiesDegreeRequirement = satisfiesDegreeRequirement;
    }

    public Boolean getAffectsGPA() {
        return affectsGPA;
    }

    public Boolean getAffectsCGPA() {
        return affectsCGPA;
    }

    public Boolean getSatisfiesDegreeRequirement() {
        return satisfiesDegreeRequirement;
    }
}
