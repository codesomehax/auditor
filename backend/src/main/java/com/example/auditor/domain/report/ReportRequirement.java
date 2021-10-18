package com.example.auditor.domain.report;

import com.example.auditor.domain.curriculum.Requirement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_requirement")
@Entity
@Builder
public class ReportRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String patterns;
    private String antipatterns;
    private Integer credit;
    private String type;


    public static ReportRequirement fromCurriculumRequirement(Requirement requirement) {
        return ReportRequirement.builder()
                .id(requirement.getId())
                .name(requirement.getName())
                .patterns(requirement.getPatterns())
                .antipatterns(requirement.getAntipatterns())
                .credit(requirement.getCredit())
                .type(requirement.getType())
                .build();
    }

}
