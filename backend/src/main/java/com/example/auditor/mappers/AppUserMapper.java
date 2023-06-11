package com.example.auditor.mappers;

import com.example.auditor.domain.appUser.AppUser;
import com.example.auditor.dto.AppUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDto appUserToDto(AppUser appUser);
}
