package com.example.auditor.controller.transcript;


import com.example.auditor.domain.transcript.StudentRecord;
import com.example.auditor.service.transcript.TranscriptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("transcript")
public class TranscriptController {

    private final TranscriptService transcriptService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentRecord createTranscript(@RequestParam("file") MultipartFile file) throws IOException {
        return transcriptService.createTranscript(file);
    }

    @GetMapping("all")
    public List<StudentRecord> getAll() {
        return transcriptService.getAll();
    }

    @GetMapping("student/{id}")
    public ResponseEntity<?> getByStudentId(@PathVariable Long id) {
        Optional<StudentRecord> optionalStudent = transcriptService.getByStudentId(id);

        if (optionalStudent.isPresent()) {
            return ResponseEntity.ok(optionalStudent.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found");
        }
    }

    @GetMapping("students/{id}")
    public ResponseEntity<?> getByStudentIds(@PathVariable Long[] id) {
        if (id == null || id.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student ids param." +
                    " Use 'students/id1,id2,id3'");
        }
        return ResponseEntity.ok(transcriptService.getByStudentIds(id));
    }

    @GetMapping("studentsGraph/{id}")
    public ResponseEntity<?> getByStudentIdsGraph(@PathVariable Long[] id) {
        if (id == null || id.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student ids param." +
                    " Use 'students/id1,id2,id3'");
        }
        return ResponseEntity.ok(transcriptService.getByStudentIdsGraph(id));
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        transcriptService.deleteById(id);
    }

}
