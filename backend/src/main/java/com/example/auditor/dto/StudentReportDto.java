package com.example.auditor.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StudentReportDto {

    @NotNull(message = "Student id must be specified")
    private Long studentId;

    @NotNull(message = "Curriculum id must be specified")
    private Long curriculumId;

}
