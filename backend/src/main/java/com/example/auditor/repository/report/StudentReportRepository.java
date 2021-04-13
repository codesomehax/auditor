package com.example.auditor.repository.report;

import com.example.auditor.domain.report.StudentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentReportRepository extends JpaRepository<StudentReport, Long> {
}
