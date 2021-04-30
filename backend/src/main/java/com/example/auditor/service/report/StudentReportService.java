package com.example.auditor.service.report;

import com.example.auditor.domain.curriculum.Requirement;
import com.example.auditor.domain.report.ReportRequirement;
import com.example.auditor.domain.report.ReportRequirementWithCourse;
import com.example.auditor.domain.report.ReportTermCourse;
import com.example.auditor.domain.report.StudentReport;
import com.example.auditor.domain.transcript.TermCourse;
import com.example.auditor.repository.curriculum.CurriculumRepository;
import com.example.auditor.repository.report.ReportRequirementRepository;
import com.example.auditor.repository.report.ReportTermCourseRepository;
import com.example.auditor.repository.report.StudentReportRepository;
import com.example.auditor.repository.transcript.StudentRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentReportService {

    private final CurriculumRepository curriculumRepository;
    private final StudentRecordRepository recordRepository;
    private final StudentReportRepository reportRepository;
    private final ReportRequirementRepository requirementRepository;
    private final ReportTermCourseRepository courseRepository;

    public StudentReport createReport(Long studentId, Long curriculumId) {
        if (reportRepository.existsById(studentId)) {
            reportRepository.deleteById(studentId);
        }
        var curriculumOpt = curriculumRepository.findById(curriculumId);
        var recordOpt = recordRepository.findById(studentId);

        if (curriculumOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid curriculum id");
        }

        if (recordOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student record id");
        }

        var curriculumEntity = curriculumOpt.get();
        var recordEntity = recordOpt.get();
        var requiredCourses = curriculumEntity
                .getRequirements()
                .stream()
                .map(ReportRequirement::fromCurriculumRequirement)
                .sorted(Comparator.comparing(ReportRequirement::getId))
                .collect(Collectors.toList());
        var completedCourses = recordEntity
                .getStudentTerms()
                .stream()
                .flatMap(x -> x.getTermCourses().stream())
                .sorted(Comparator.comparingInt(TermCourse::getCredits))
                .collect(Collectors.toList());
        var resultReport = new StudentReport();
        var completedRequirements = new ArrayList<ReportRequirementWithCourse>();
        var requirementsToRemove = new ArrayList<ReportRequirement>();

        for (var course : requiredCourses) {
            if (course.getPatterns().contains(",")) {
                for (var pat : course.getPatterns().split(",")) {
                    TermCourse candidate = null;
                    for (var completed : completedCourses) {
                        if (completed.getCode().toLowerCase().startsWith(pat.toLowerCase())
                                && completed.getCredits() >= course.getCredit()) {
                            candidate = completed;
                        }
                    }
                    if (candidate != null) {
                        completedRequirements
                                .add(
                                        ReportRequirementWithCourse
                                                .builder()
                                                .course(ReportTermCourse.fromTranscriptTermCourse(candidate))
                                                .requirement(course)
                                                .build()
                                );

                        completedCourses.remove(candidate);
                        requirementsToRemove.add(course);
                    }

                }
            } else {
                var pattern = course.getPatterns().strip();
                TermCourse candidate = null;
                for (var completed : completedCourses) {
                    if (completed.getCode().toLowerCase().startsWith(pattern.toLowerCase())
                            && completed.getCredits() >= course.getCredit()) {
                        candidate = completed;
                    } else {
                        if (pattern.equals("*") && completed.getCredits() >= course.getCredit()) {
                            candidate = completed;
                        }
                    }
                }
                if (candidate != null) {
                    completedRequirements
                            .add(
                                    ReportRequirementWithCourse
                                            .builder()
                                            .course(ReportTermCourse.fromTranscriptTermCourse(candidate))
                                            .requirement(course)
                                            .build()
                            );

                    completedCourses.remove(candidate);
                    requirementsToRemove.add(course);
                }
            }
        }
        requiredCourses.removeAll(requirementsToRemove);
        resultReport.setUnmappedCourses(completedCourses
                .stream()
                .map(ReportTermCourse::fromTranscriptTermCourse)
                .collect(Collectors.toList()));
        resultReport.setUnmappedRequirements(new ArrayList<>(requiredCourses));
        resultReport.setCurriculumId(curriculumId);
        resultReport.setId(recordEntity.getId());
        resultReport.setCredits(recordEntity.getCreditsEarned());
        resultReport.setCompleteRequirements(completedRequirements);
        return reportRepository.save(resultReport);

    }

    public Optional<StudentReport> getById(Long id) {
        return reportRepository.findById(id);
    }

    public void mapRequirement(Long reportId, Long courseId, Long requirementId) {
        var reportOpt = reportRepository.findById(reportId);
        if (reportOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid report id");
        }
        var courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid course id");
        }
        var requirementOpt = requirementRepository.findById(requirementId);
        if (requirementOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid requirement id");
        }
        var reportEntity = reportOpt.get();
        for (var item : reportEntity.getCompleteRequirements()
                .stream().map(ReportRequirementWithCourse::getRequirement)
                .collect(Collectors.toList())
        ) {
            if (item.getId().equals(requirementId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "This requirement is already assigned. To re-assign detach it from the mapped course");
            }
        }

        for (var item : reportEntity.getCompleteRequirements()
                .stream().map(ReportRequirementWithCourse::getCourse)
                .collect(Collectors.toList())
        ) {
            if (item.getId().equals(courseId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "This course is already assigned. To re-assign detach it from the mapped requirement");
            }
        }
        reportEntity.getCompleteRequirements()
                .add(
                        new ReportRequirementWithCourse(
                                null,
                                requirementOpt.get(),
                                courseOpt.get()
                        )
                );
        reportEntity.getUnmappedCourses().remove(courseOpt.get());
        reportEntity.getUnmappedRequirements().remove(requirementOpt.get());
        reportRepository.save(reportEntity);
    }

    public void detachRequirement(Long reportId, Long requirementId) {
        var reportOpt = reportRepository.findById(reportId);
        if (reportOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid report id");
        }
        var reportEntity = reportOpt.get();
        ReportRequirementWithCourse reportRequirementWithCourse = null;
        for (var i : reportEntity.getCompleteRequirements()) {
            if (i.getRequirement().getId().equals(requirementId)) {
                reportEntity.getUnmappedRequirements().add(i.getRequirement());
                reportEntity.getUnmappedCourses().add(i.getCourse());
                reportRequirementWithCourse = i;
                break;
            }
        }
        if (reportRequirementWithCourse != null) {
            reportEntity.getCompleteRequirements().remove(reportRequirementWithCourse);
            reportRepository.save(reportEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid requirement id to detach");
        }
    }

    public void detachCompletedCourse(Long reportId, Long courseId) {
        var reportOpt = reportRepository.findById(reportId);
        if (reportOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid report id");
        }
        var reportEntity = reportOpt.get();
        ReportRequirementWithCourse reportRequirementWithCourse = null;
        for (var i : reportEntity.getCompleteRequirements()) {
            if (i.getCourse().getId().equals(courseId)) {
                reportEntity.getUnmappedCourses().add(i.getCourse());
                reportEntity.getUnmappedRequirements().add(i.getRequirement());
                reportRequirementWithCourse = i;
                break;
            }
        }
        if (reportRequirementWithCourse != null) {
            reportEntity.getCompleteRequirements().remove(reportRequirementWithCourse);
            reportRepository.save(reportEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid course id to detach");
        }
    }

    public void deleteById(Long id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid report id");
        }
    }

}
