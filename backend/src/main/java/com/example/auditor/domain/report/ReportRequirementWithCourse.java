package com.example.auditor.domain.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "report_requirement_with_course")
public class ReportRequirementWithCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private ReportRequirement requirement;

    @ManyToOne(cascade = CascadeType.ALL)
    private ReportTermCourse course;

}
