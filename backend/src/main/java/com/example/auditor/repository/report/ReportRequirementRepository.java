package com.example.auditor.repository.report;

import com.example.auditor.domain.report.ReportRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRequirementRepository extends JpaRepository<ReportRequirement, Long> {
}
