package com.example.auditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotNull(message = "userMail year cannot be null", groups = Create.class)
    private String userMail;

    @NotNull(message = "password year cannot be null", groups = Create.class)
    private String password;

    public interface Create {

    }
}
