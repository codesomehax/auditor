package com.example.auditor.repository.transcript;

import com.example.auditor.domain.transcript.TermCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<TermCourse, Long> {
}
