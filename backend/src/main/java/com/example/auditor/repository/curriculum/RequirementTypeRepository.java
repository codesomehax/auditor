package com.example.auditor.repository.curriculum;

import com.example.auditor.domain.curriculum.RequirementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementTypeRepository extends JpaRepository<RequirementType, Long> {
}
