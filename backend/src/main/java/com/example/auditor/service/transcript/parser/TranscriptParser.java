package com.example.auditor.service.transcript.parser;

import com.example.auditor.domain.transcript.StudentRecord;
import com.example.auditor.domain.transcript.TermCourse;
import com.example.auditor.domain.transcript.StudentTerm;
import com.example.auditor.service.transcript.parser.LetterGrade.*;
import com.example.auditor.service.transcript.parser.util.IntermediateTerm;
import com.example.auditor.service.transcript.parser.util.Pair;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import io.swagger.v3.oas.models.links.Link;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Data
@Component
@Slf4j
@Scope(value = "prototype")
public class TranscriptParser {

    private final String text;
    private final LinkedList<IntermediateTerm> intermediateTerms;

    public TranscriptParser(String text) {
        this.text = text;
        this.intermediateTerms = new LinkedList<>();
    }

    public StudentRecord buildStudentRecord() {

        double gpa = 0d;
        double gradePointsTotal = 0d;
        Integer creditsEnrolledTotal = 0;
        Integer creditsEarnedTotal = 0;
        Integer creditsGradedTotal = 0;
        Integer currentSemester = 1;

        LinkedList<StudentTerm> studentTerms = buildStudentTerms();
        for (StudentTerm studentTerm : studentTerms) {

            gradePointsTotal += studentTerm.getGradePointsThisTerm();
            creditsEnrolledTotal += studentTerm.getCreditsEnrolledThisTerm();
            creditsEarnedTotal += studentTerm.getCreditsEarnedThisTerm();
            creditsGradedTotal += studentTerm.getCreditsGradedThisTerm();

            if (studentTerm.getName().startsWith("Fall") || studentTerm.getName().startsWith("Spring")) {
                currentSemester++;
            }
        }

        gpa = (double) studentTerms.getLast().getTermCumulativeGpa();

        return StudentRecord.builder()
                .id(getStudentId())
                .name(getStudentName())
                .schoolName(getSchoolName())
                .major(getStudentMajor())
                .admissionSemester(getAdmissionSemester())
                .gpa(round(gpa))
                .gradePointsTotal(gradePointsTotal)
                .creditsEnrolledTotal(creditsEnrolledTotal)
                .creditsEarnedTotal(creditsEarnedTotal)
                .creditsGradedTotal(creditsGradedTotal)
                .currentSemester(currentSemester)
                .studentTerms(studentTerms)
        .build();

    }


    private LinkedList<StudentTerm> buildStudentTerms() {

        LinkedList<StudentTerm> studentTerms = new LinkedList<>();


        IntersemesterExchange intersemesterExchange = IntersemesterExchange.builder()
                .creditsGradedTotal(0)
                .gradePointsTotal(0d)
                .retakenCourses(new LinkedList<TermCourse>())
                .build();

        buildIntermediateTerms();
        for (IntermediateTerm intermediateTerm : intermediateTerms) {

            studentTerms.add(
                    buildStudentTerm(intermediateTerm, intersemesterExchange)
            );
        }

        return studentTerms;
    }


    private StudentTerm buildStudentTerm (
            IntermediateTerm intermediateTerm, IntersemesterExchange intersemesterExchange
    ) {

        String name = text.substring(
                intermediateTerm.getPosition().getFirst(),
                intermediateTerm.getPosition().getSecond()
        ).strip();
        name = name.replaceAll("[\\t\\n\\r]+"," ");

        Integer creditsEnrolledThisTerm = 0;
        Integer creditsEarnedThisTerm = 0;
        Integer creditsGradedThisTerm = 0;
        Integer creditsGradedCumulative = intersemesterExchange.getCreditsGradedTotal();
        Integer creditsGradedTotal = intersemesterExchange.getCreditsGradedTotal();

        double gradePointsThisTerm = 0d;
        double gradePointsCumulative = intersemesterExchange.getGradePointsTotal();
        double gradePointsTotal = intersemesterExchange.getGradePointsTotal();

        LinkedList<TermCourse> retakenCourses = intersemesterExchange.getRetakenCourses();

        LinkedList<TermCourse> termCourses = buildTermCourses(intermediateTerm.getCourseRegion());
        for (TermCourse termCourse : termCourses) {

            int courseCredits = termCourse.getCredits();
            double courseGradePoint = termCourse.getGradePoint();
            double courseGradePointWeighted = round(courseGradePoint * courseCredits );

            creditsEnrolledThisTerm += courseCredits;

            if (termCourse.getLetterGradeModifiedInstance().getSatisfiesDegreeRequirement()) {

                creditsEarnedThisTerm += courseCredits;
            }

            if (termCourse.getLetterGradeModifiedInstance().getLetterGrade().getGroup().getAffectsSemesterGPA()) {

                creditsGradedThisTerm += courseCredits;
                creditsGradedCumulative += courseCredits;
                gradePointsThisTerm += courseGradePointWeighted;
                gradePointsCumulative += courseGradePointWeighted;
            }

            if (termCourse.getLetterGradeModifiedInstance().getLetterGrade().getGroup().getAffectsCGPA()) {

                creditsGradedTotal += courseCredits;
                gradePointsTotal += courseGradePointWeighted;

            }

             if (termCourse.getLetterGradeModifiedInstance().getLetterGradeModifier() == LetterGradeModifier.DOUBLESTAR) {

                 retakenCourses.add(termCourse);

             } else {

                 LinkedList<TermCourse> cancelledRetakes = cancelRetakes(retakenCourses, termCourse);
                 for (TermCourse cancelledRetake : cancelledRetakes) {

                     int cancelledRetakeCredits = cancelledRetake.getCredits();
                     double cancelledRetakeGradePoints = cancelledRetake.getGradePoint();

                     creditsGradedCumulative -= cancelledRetakeCredits;
                     creditsGradedTotal -= cancelledRetakeCredits;

                     gradePointsCumulative -= cancelledRetakeGradePoints;
                     gradePointsTotal -= cancelledRetakeGradePoints;
                 }
             }
        }

        intersemesterExchange.setCreditsGradedTotal(creditsGradedTotal);
        intersemesterExchange.setGradePointsTotal(gradePointsTotal);
        intersemesterExchange.setRetakenCourses(retakenCourses);


        double termGpa = calculateGPA(gradePointsThisTerm, creditsGradedThisTerm);
        double termCumulativeGpa = calculateGPA(gradePointsCumulative, creditsGradedCumulative);

        return StudentTerm.builder()
                    .id(null)
                    .name(name)
                    .creditsEnrolledThisTerm(creditsEnrolledThisTerm)
                    .creditsEarnedThisTerm(creditsEarnedThisTerm)
                    .creditsGradedThisTerm(creditsGradedThisTerm)
                    .creditsGradedCumulative(creditsGradedCumulative)
                    .creditsGradedTotal(creditsGradedTotal)
                    .gradePointsThisTerm(gradePointsThisTerm)
                    .gradePointsCumulative(gradePointsCumulative)
                    .gradePointsTotal(gradePointsTotal)
                    .termCourses(termCourses)
                    .termGpa(termGpa)
                    .termCumulativeGpa(termCumulativeGpa)
                .build();
    }


    private double calculateGPA(double gradePoints, int creditsGraded) {

        double GPA = 0d;

        if (creditsGraded != 0) {

            GPA = round(gradePoints / creditsGraded);
        }

        return GPA;
    }

    private LinkedList<TermCourse> cancelRetakes(LinkedList<TermCourse> retakenCourses, TermCourse termCourse) {

        LinkedList<TermCourse> cancelledRetakes = new LinkedList<>();
        for (TermCourse retakenCourse : retakenCourses) {

            if (retakenCourse.getCode().equals(termCourse.getCode())) {

                cancelledRetakes.add(retakenCourse);
            }
        }

        retakenCourses.removeAll(cancelledRetakes);

        return cancelledRetakes;
    }


    public TermCourse buildTermCourse(int start, int end) throws LiteralNotMatchedException {
        String courseCode = text.substring(start, end);
        Pattern p = Pattern.compile(TranscriptParserKeyword.CREDIT_AND_GRADE_POINT, Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        m.region(end, text.length());
        String credits = Strings.EMPTY;
        String grade = Strings.EMPTY;
        String courseLetterGradeLiteral = Strings.EMPTY;
        if (m.find()) {
            courseLetterGradeLiteral = m.group(1);
            credits = m.group(2);
            grade = m.group(3);
        }

        Integer courseCredits = credits.isEmpty() ? 0 : Integer.parseInt(credits);
        Double courseGradePoint = 0d;
        if(!grade.equals("n/a")) {
            courseGradePoint = credits.isEmpty() ? 0f : Double.parseDouble(grade);
        }

        LetterGradeModifiedInstance letterGradeModifiedInstance = new LetterGradeModifiedInstance(courseLetterGradeLiteral);

        return TermCourse.builder()
                .id(null)
                .code(courseCode)
                .credits(courseCredits)
                .gradePoint(letterGradeModifiedInstance.getLetterGrade().getGradePoint())
                .letterGradeModifiedInstance(letterGradeModifiedInstance)
                .letterGradeLiteral(letterGradeModifiedInstance.getLetterGrade().getLiteral())
                .letterGradeModifierLiteral(letterGradeModifiedInstance.getLetterGradeModifier().getLiteral())
        .build();

    }


    public LinkedList<TermCourse> buildTermCourses(Pair courseRegion) {
        Pattern p = Pattern.compile(TranscriptParserKeyword.COURSE_CODE, Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        m.region(courseRegion.getFirst(), courseRegion.getSecond());
        LinkedList<TermCourse> courses = new LinkedList<>();
        while (m.find()) {
            try {
                courses.add(buildTermCourse(m.start(), m.end()));
            } catch (LiteralNotMatchedException e) {
                e.printStackTrace();
            }
        }
        return courses;
    }

    private void buildIntermediateTerms() {
        Pattern p = Pattern.compile(TranscriptParserKeyword.TERM, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        while (m.find()) {
          if (!intermediateTerms.isEmpty()) {
            intermediateTerms.getLast().getCourseRegion().setSecond(m.start());
          }
          IntermediateTerm t = new IntermediateTerm(new Pair(m.start(), m.end()), new Pair(m.end(), text.length()));
          intermediateTerms.add(t);
        }
    }


    private String getBetweenPrefixAndSuffix(String prefix, String suffix) {
        String focusTextCaptureGroup = "(.+?)"; // it has to be non greedy
        String regex = prefix + focusTextCaptureGroup + suffix;
        String result = "";
        Pattern p =  Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m  = p.matcher(text);
        if(m.find()){
          result = m.group(1).trim();
        }else{
          result =  UUID.randomUUID().toString();
        }
        return result;
    }


    public String getStudentName() {

        return getBetweenPrefixAndSuffix(TranscriptParserKeyword.STUDENT_NAME, TranscriptParserKeyword.ID);
    }

    public long getStudentId() {

        return Long.parseLong(getBetweenPrefixAndSuffix(TranscriptParserKeyword.STUDENT_ID, TranscriptParserKeyword.ENDLINE));
    }

    public String getSchoolName() {

        return getBetweenPrefixAndSuffix(TranscriptParserKeyword.SCHOOL_NAME, TranscriptParserKeyword.PROGRAM);
    }

    public String getStudentMajor() {

        return getBetweenPrefixAndSuffix(TranscriptParserKeyword.STUDENT_MAJOR, TranscriptParserKeyword.ENDLINE);
    }

    public String getAdmissionSemester() {

        return getBetweenPrefixAndSuffix(TranscriptParserKeyword.ADMISSION_SEMESTER, TranscriptParserKeyword.ENDLINE);
    }


    private double round(double d) {

        double retval = 0.0;
        try {
            retval = BigDecimal.valueOf(d)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }

        return retval;


    }
}


