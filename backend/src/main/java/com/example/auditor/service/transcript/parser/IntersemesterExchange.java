package com.example.auditor.service.transcript.parser;

import com.example.auditor.domain.transcript.TermCourse;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;

@Data
@Builder
public class IntersemesterExchange {

    private Integer creditsGradedTotal;
    private double gradePointsTotal;
    private LinkedList<TermCourse> retakenCourses;

}
