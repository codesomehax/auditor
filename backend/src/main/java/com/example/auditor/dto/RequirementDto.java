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

    public interface Create {

    }
}
