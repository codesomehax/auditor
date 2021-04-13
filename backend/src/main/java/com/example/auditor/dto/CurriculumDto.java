package com.example.auditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumDto {

    @NotNull(message = "Curriculum major cannot be null", groups = Create.class)
    private String major;

    @NotNull(message = "Curriculum year cannot be null", groups = Create.class)
    private Integer year;

    public interface Create {

    }

}
