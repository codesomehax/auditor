package com.example.auditor;

import com.example.auditor.domain.appUser.AppUser;
import com.example.auditor.domain.appUser.AppUserRole;
import com.example.auditor.repository.appUser.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseInitialization implements InitializingBean {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() throws Exception {
        AppUser john = AppUser
                .builder()
                .email("john@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .roles(Set.of(AppUserRole.CURRICULUM))
                .build();

        AppUser sarah = AppUser
                .builder()
                .email("sarah@gmail.com")
                .password(passwordEncoder.encode("4321"))
                .roles(Set.of(AppUserRole.values()))
                .build();

        appUserRepository.save(john);
        appUserRepository.save(sarah);
    }
}
