package com.example.auditor.repository.appUser;

import com.example.auditor.domain.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findById(int id);
    Optional<AppUser> findByEmail(String email);
}
