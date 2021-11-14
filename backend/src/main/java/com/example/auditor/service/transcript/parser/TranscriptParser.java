package com.example.auditor.service.transcript.parser;

import com.example.auditor.domain.transcript.TermCourse;
import com.example.auditor.domain.transcript.StudentTerm;
import com.example.auditor.service.transcript.parser.util.IntermediateTerm;
import com.example.auditor.service.transcript.parser.util.Pair;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
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

@Value("${audit.parser.transcript.regex.course-code}")
public String courseCode;

@Value("${audit.parser.transcript.regex.credit-and-gpa}")
public String creditAndGradePoint;

@Value("${audit.parser.transcript.regex.prefix.student-name}")
public String studentName;

@Value("${audit.parser.transcript.regex.prefix.student-id}")
public String studentId;

@Value("${audit.parser.transcript.regex.prefix.school-name}")
public String schoolName;

@Value("${audit.parser.transcript.regex.prefix.student-major}")
public String studentMajor;

@Value("${audit.parser.transcript.regex.prefix.admission-semester}")
public String admissionSemester;

@Value("${audit.parser.transcript.regex.prefix.term}")
public String term;

@Value("${audit.parser.transcript.regex.prefix.overall}")
public String overall;

@Value("${audit.parser.transcript.regex.suffix.end-line}")
public String endLine;

@Value("${audit.parser.transcript.regex.suffix.program}")
public String program;

@Value("${audit.parser.transcript.regex.suffix.id}")
public String id;

@Value("${audit.parser.transcript.regex.gpa}")
public String gpa;

@Value("${audit.parser.transcript.regex.cumulative}")
public String cumulative;

private final String text;

private final LinkedList<IntermediateTerm> intermediateTerms;

private String cumulativeGPA;

private String creditsEarned;



public TranscriptParser(String text) {
    this.text = text;
    this.intermediateTerms = new LinkedList<>();
}

public void buildIntermediateTerms() {
    Pattern p = Pattern.compile(term, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    Matcher m = p.matcher(text);
    while (m.find()) {
      if (!intermediateTerms.isEmpty()) {
        intermediateTerms.getLast().getCourseRegion().setSecond(m.start());
      }
      IntermediateTerm t = new IntermediateTerm(new Pair(m.start(), m.end()), new Pair(m.end(), text.length()));
      intermediateTerms.add(t);
    }
}

public LinkedList<StudentTerm> getTerms() {
    buildIntermediateTerms();
    buildOveralls();
    LinkedList<StudentTerm> studentTerms = new LinkedList<>();
    for (IntermediateTerm intermediateTerm : intermediateTerms) {
        studentTerms.add(buildTerm(intermediateTerm));
    }
    return studentTerms;
}

private void buildOveralls() {
    IntermediateTerm last = intermediateTerms.getLast();
    Pair region = last.getCourseRegion();
    Pattern p = Pattern.compile(cumulative);
    Matcher m = p.matcher(text);
    m.region(region.getFirst(), region.getSecond());
    m.find();
    this.creditsEarned = m.group(1);
    this.cumulativeGPA = m.group(2);
}

public StudentTerm buildTerm(IntermediateTerm intermediateTerm) {
    String termName = text.substring(
        intermediateTerm.getPosition().getFirst(),
        intermediateTerm.getPosition().getSecond()
    ).strip();
    termName = termName.replaceAll("[\\t\\n\\r]+"," "); // remove newlines
    LinkedList<TermCourse> termCourses = buildCourses(intermediateTerm.getCourseRegion());
    String textTermGpa = getTermGpa(intermediateTerm.getCourseRegion());
    Double termGpa = Double.parseDouble(textTermGpa);


    double slicedGpa = BigDecimal.valueOf(termGpa)
        .setScale(2, RoundingMode.HALF_UP)
        .doubleValue();

    return new StudentTerm(null, termName, termCourses, slicedGpa);
}

public LinkedList<TermCourse> buildCourses(Pair courseRegion) {
    Pattern p = Pattern.compile(courseCode, Pattern.MULTILINE);
    Matcher m = p.matcher(text);
    m.region(courseRegion.getFirst(), courseRegion.getSecond());
    LinkedList<TermCourse> cours = new LinkedList<>();
    while (m.find()) {
      cours.add(buildCourse(m.start(), m.end()));
    }
    return cours;
}

public TermCourse buildCourse(int start, int end) {
    String courseCode = text.substring(start, end);
    Pattern p = Pattern.compile(creditAndGradePoint, Pattern.MULTILINE);
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

    LetterGrade letterGrade = null;

    try {
        letterGrade = LetterGrade.match(courseLetterGradeLiteral);

    } catch (LiteralNotMatchedException e) {

        e.printStackTrace();
    }

    return TermCourse.builder()
                .id(null)
                .code(courseCode)
                .credits(courseCredits)
                .gradePoint(letterGrade.getGradePoint())
                .letterGrade(letterGrade.getLiteral())
                .satisfiesDegreeRequirement(letterGrade.getLetterGradeGroup().getSatisfiesDegreeRequirement())
                .affectsGPA(letterGrade.getLetterGradeGroup().getAffectsGPA())
                .affectsCGPA(letterGrade.getLetterGradeGroup().getAffectsCGPA())
            .build();
}

private String getTermGpa(Pair region){
    String regex = gpa + "\\s*(\\d\\.\\d{1,2}|\\d)";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(text);
    m.region(region.getFirst(), region.getSecond());
    if(m.find()){
      return m.group(1);
    }
    return "0";
}

// method overloading is way better than optional argument shit!
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
return getBetweenPrefixAndSuffix(studentName, id);
}

public long getStudentId() {
return Long.parseLong(getBetweenPrefixAndSuffix(studentId, endLine));
}

public String getSchoolName() {
return getBetweenPrefixAndSuffix(schoolName, program);
}

public String getStudentMajor() {
return getBetweenPrefixAndSuffix(studentMajor, endLine);
}
// to restore service
public String getAdmissionSemester() {
return getBetweenPrefixAndSuffix(admissionSemester, endLine);
}

public Float getOverallGPA(){
return Float.parseFloat(cumulativeGPA);
//    return 0f;
}


public Integer getOverallCreditsEnrolled(){
//    Matcher m = getOverallMatcher();
//    if(m.find()){
//      return Integer.parseInt(m.group(3));
//    }
return 0;
}

public Integer getOverallCreditsEarned(){
return Integer.parseInt(creditsEarned);
}


private int getStartRegion(){
Pattern p = Pattern.compile(overall, Pattern.MULTILINE);
Matcher m = p.matcher(text);
if(m.find()){
  return m.end();
}
return 0;
}
}


