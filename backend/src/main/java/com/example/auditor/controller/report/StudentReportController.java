package com.example.auditor.controller.report;

import com.example.auditor.domain.report.StudentReport;
import com.example.auditor.dto.StudentReportDto;
import com.example.auditor.service.report.StudentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("{reportId}/detachCourses")
    public void detachCourse(@PathVariable Long reportId, @RequestParam Long[] courseIds) {
        for (var i : courseIds) {
            reportService.detachCompletedCourse(reportId, i);
        }
    }

    @PostMapping("{reportId}/detachRequirements")
    public void detachRequirement(@PathVariable Long reportId, @RequestParam Long[] requirementIds) {
        for (var i : requirementIds) {
            reportService.detachRequirement(reportId, i);
        }
    }

    @PostMapping("{reportId}/mapRequirement")
    public void mapRequirement(@PathVariable Long reportId,
                               @RequestParam Long[] courseIds,
                               @RequestParam Long[] requirementIds) {
        if (courseIds.length != requirementIds.length) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of courses and requirements to map is not equal");
        }
        for (int i = 0; i < courseIds.length; i++) {
            reportService.mapRequirement(reportId, courseIds[i], requirementIds[i]);
        }
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        reportService.deleteById(id);
    }

}
