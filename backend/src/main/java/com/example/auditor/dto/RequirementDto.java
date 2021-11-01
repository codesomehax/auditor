package com.example.auditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequirementDto {

    private String name;
    private String type;
    private Integer credit;
    private String patterns;
    private String antipatterns;
    private Integer semester;

    public String getPatterns() {

        String patterns = this.patterns;
        if (patterns.equals("")) {

            patterns = name.split("-")[0].strip();
        }

        return patterns;
    }


    public interface Create {

    }
}
