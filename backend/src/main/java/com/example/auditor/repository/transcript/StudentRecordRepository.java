package com.example.auditor.repository.transcript;

import com.example.auditor.domain.transcript.StudentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRecordRepository extends JpaRepository<StudentRecord, Long> {

}
