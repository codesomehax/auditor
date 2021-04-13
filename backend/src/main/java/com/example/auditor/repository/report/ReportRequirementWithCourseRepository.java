package com.example.auditor.repository.report;

import com.example.auditor.domain.report.ReportRequirementWithCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRequirementWithCourseRepository extends JpaRepository<ReportRequirementWithCourse, Long> {
}
