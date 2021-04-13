package com.example.auditor.domain.report;


import com.example.auditor.domain.curriculum.RequirementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_requirement_type")
@Entity
@Builder
public class ReportRequirementType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    public static ReportRequirementType fromCurriculumRequirementType(RequirementType type) {
        return ReportRequirementType.builder()
                .id(type.getId())
                .name(type.getName())
                .build();
    }

}
