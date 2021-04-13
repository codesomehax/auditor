package com.example.auditor.repository.report;

import com.example.auditor.domain.report.ReportTermCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTermCourseRepository extends JpaRepository<ReportTermCourse, Long> {
}
