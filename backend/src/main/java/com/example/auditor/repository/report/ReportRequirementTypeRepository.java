package com.example.auditor.repository.report;

import com.example.auditor.domain.report.ReportRequirementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRequirementTypeRepository extends JpaRepository<ReportRequirementType, Long> {
}
