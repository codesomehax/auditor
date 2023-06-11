package com.example.auditor.dto;

import com.example.auditor.domain.appUser.AppUserRole;
import lombok.Data;

import java.util.List;

@Data
public class AppUserDto {
    private final int id;
    private final String email;
    private final List<AppUserRole> roles;
}
