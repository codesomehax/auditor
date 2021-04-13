package com.example.auditor.repository.transcript;

import com.example.auditor.domain.transcript.StudentTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<StudentTerm, Long> {
}
