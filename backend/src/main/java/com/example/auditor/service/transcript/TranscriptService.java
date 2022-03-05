package com.example.auditor.service.transcript;

import com.example.auditor.domain.transcript.StudentRecord;
import com.example.auditor.domain.transcript.StudentTerm;
import com.example.auditor.repository.transcript.StudentRecordRepository;
import com.example.auditor.service.transcript.parser.TranscriptParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranscriptService {

    private final StudentRecordRepository studentRecordRepository;
    private final BeanFactory beanFactory;

    public StudentRecord createTranscript(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = beanFactory.getBean(PDFTextStripper.class);
        String transcriptText = stripper.getText(document);
        TranscriptParser tParser = beanFactory.getBean(TranscriptParser.class, transcriptText);

        document.close();

        return studentRecordRepository.save(tParser.buildStudentRecord());
    }

    public List<StudentRecord> getAll() {
        return studentRecordRepository.findAll();
    }

    public Optional<StudentRecord> getByStudentId(Long id) {
        return studentRecordRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (studentRecordRepository.existsById(id)) {
            studentRecordRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student record id");
        }
    }

    public List<StudentRecord> getByStudentIds(Long[] ids) {
        return Arrays.stream(ids)
                .map(studentRecordRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    public Map<Object, Object> getByStudentIdsGraph(Long[] ids) {
        Map<Object, Object> resultMap = new HashMap<>();
        var records = Arrays.stream(ids)
                .map(studentRecordRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (records.isEmpty()) {
            return Map.of();
        }

        var terms = records.stream()
                .flatMap(x -> x.getStudentTerms().stream())
                .map(StudentTerm::getName)
                .distinct()
                .sorted((term1, term2) -> {
                    var term1Lowercase = term1.toLowerCase().strip();
                    var term2Lowercase = term2.toLowerCase().strip();
                    var term1Year = term1Lowercase.split(" ")[1].strip();
                    var term2Year = term2Lowercase.split(" ")[1].strip();
                    var term1Season = term1Lowercase.split(" ")[0].strip();
                    var term2Season = term2Lowercase.split(" ")[0].strip();

                    if (!term1Year.equals(term2Year)) {
                        return term1Year.compareTo(term2Year);
                    } else {
                        return TermTypeOrder.getFromString(term1Season)
                                .compareTo(TermTypeOrder.getFromString(term2Season));
                    }
                })
                .collect(Collectors.toList());

        var studentTerms = records
                .stream()
                .collect(
                        Collectors.toMap(StudentRecord::getId,
                                y -> y.getStudentTerms()
                                        .stream()
                                        .map(StudentTerm::getName)
                                        .collect(Collectors.toList())
                        )
                );
        var students = records
                .stream()
                .map(StudentRecord::getId)
                .collect(Collectors.toList());
        var studentToTerm = records
                .stream()
                .collect(
                        Collectors.toMap(StudentRecord::getId,
                                y -> y.getStudentTerms()
                                        .stream()
                                        .map(
                                                x -> Map.of("termName", x.getName(),
                                                        "termGpa", x.getTermGpa())
                                        )
                                        .collect(Collectors.toList())
                        )
                );

        resultMap.put("terms", terms);
        resultMap.put("graph", studentToTerm);
        resultMap.put("students", students);
        resultMap.put("studentTerms", studentTerms);

        return resultMap;
    }

}

enum TermTypeOrder {
    spring, summer, fall;

    public static TermTypeOrder getFromString(String string) {
        return valueOf(string.toLowerCase());
    }

}
