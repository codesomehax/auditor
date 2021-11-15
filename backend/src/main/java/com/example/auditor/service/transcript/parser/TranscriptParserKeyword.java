package com.example.auditor.service.transcript.parser;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TranscriptParserKeyword {

    public static String COURSE_CODE;
    public static String CREDIT_AND_GRADE_POINT;
    public static String STUDENT_NAME;
    public static String STUDENT_ID;
    public static String SCHOOL_NAME;
    public static String STUDENT_MAJOR;
    public static String ADMISSION_SEMESTER;
    public static String TERM;
    public static String OVERALL;
    public static String ENDLINE;
    public static String PROGRAM;
    public static String ID;
    public static String GPA;
    public static String CUMULATIVE;


    @Value("${audit.parser.transcript.regex.course-code}")
    public void setCourseCode(String courseCode) {
        COURSE_CODE = courseCode;
    }

    @Value("${audit.parser.transcript.regex.credit-and-gpa}")
    public void setCreditAndGradePoint(String creditAndGradePoint) {
        CREDIT_AND_GRADE_POINT = creditAndGradePoint;
    }

    @Value("${audit.parser.transcript.regex.prefix.student-name}")
    public void setStudentName(String studentName) {
        STUDENT_NAME = studentName;
    }

    @Value("${audit.parser.transcript.regex.prefix.student-id}")
    public void setStudentId(String studentId) {
        STUDENT_ID = studentId;
    }

    @Value("${audit.parser.transcript.regex.prefix.school-name}")
    public void setSchoolName(String schoolName) {
        SCHOOL_NAME = schoolName;
    }

    @Value("${audit.parser.transcript.regex.prefix.student-major}")
    public void setStudentMajor(String studentMajor) {
        STUDENT_MAJOR = studentMajor;
    }

    @Value("${audit.parser.transcript.regex.prefix.admission-semester}")
    public void setAdmissionSemester(String admissionSemester) {
        ADMISSION_SEMESTER = admissionSemester;
    }

    @Value("${audit.parser.transcript.regex.prefix.term}")
    public void setTERM(String TERM) {
        TranscriptParserKeyword.TERM = TERM;
    }

    @Value("${audit.parser.transcript.regex.prefix.overall}")
    public void setOVERALL(String OVERALL) {
        TranscriptParserKeyword.OVERALL = OVERALL;
    }

    @Value("${audit.parser.transcript.regex.suffix.end-line}")
    public void setENDLINE(String ENDLINE) {
        TranscriptParserKeyword.ENDLINE = ENDLINE;
    }

    @Value("${audit.parser.transcript.regex.suffix.program}")
    public void setPROGRAM(String PROGRAM) {
        TranscriptParserKeyword.PROGRAM = PROGRAM;
    }

    @Value("${audit.parser.transcript.regex.suffix.id}")
    public void setID(String ID) {
        TranscriptParserKeyword.ID = ID;
    }

    @Value("${audit.parser.transcript.regex.gpa}")
    public void setGPA(String GPA) {
        TranscriptParserKeyword.GPA = GPA;
    }

    @Value("${audit.parser.transcript.regex.cumulative}")
    public void setCUMULATIVE(String CUMULATIVE) {
        TranscriptParserKeyword.CUMULATIVE = CUMULATIVE;
    }

}
