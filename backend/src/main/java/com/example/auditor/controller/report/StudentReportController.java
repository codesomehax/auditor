package com.example.auditor.controller.report;

import com.example.auditor.domain.report.StudentReport;
import com.example.auditor.dto.StudentReportDto;
import com.example.auditor.service.report.StudentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("report")
public class StudentReportController {

    private final StudentReportService reportService;

    @PostMapping
    public StudentReport create(@RequestBody
                                @Validated StudentReportDto dto) {
        return reportService.createReport(dto.getStudentId(), dto.getCurriculumId());
    }

    @GetMapping("{id}")
    public Map<Object, Object> getByID(@PathVariable Long id) {
        var report = reportService.getById(id);
        return Map.of(
                "data", report.isPresent(),
                "content", report.orElse(new StudentReport())
        );
    }

    @PostMapping("{reportId}/detachCourse")
    public void detachCourse(@PathVariable Long reportId, @RequestParam Long courseId) {
        reportService.detachCompletedCourse(reportId, courseId);
    }

    @PostMapping("{reportId}/detachRequirement")
    public void detachRequirement(@PathVariable Long reportId, @RequestParam Long requirementId) {
        reportService.detachRequirement(reportId, requirementId);
    }

    @PostMapping("{reportId}/mapRequirement")
    public void mapRequirement(@PathVariable Long reportId,
                               @RequestParam Long courseId,
                               @RequestParam Long requirementId) {
        reportService.mapRequirement(reportId, courseId, requirementId);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        reportService.deleteById(id);
    }

}
