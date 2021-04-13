package com.example.auditor.domain.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "student_report")
public class StudentReport {

    @Id
    private Long id;

    private Long curriculumId;

    private Integer credits;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ReportRequirementWithCourse> completeRequirements;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ReportRequirement> unmappedRequirements;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ReportTermCourse> unmappedCourses;

}
