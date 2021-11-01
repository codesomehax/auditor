package com.example.auditor.controller.curriculum;

import com.example.auditor.domain.curriculum.Curriculum;
import com.example.auditor.dto.CurriculumDto;
import com.example.auditor.dto.RequirementDto;
import com.example.auditor.service.curriculum.CurriculumExportService;
import com.example.auditor.service.curriculum.CurriculumService;
import com.example.auditor.service.curriculum.CurriculumImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("curriculum")
public class CurriculumController {

    private final CurriculumImportService parseService;
    private final CurriculumExportService curriculumExportService;
    private final CurriculumService curriculumService;

    @PostMapping
    public Curriculum createCurriculum(@Validated(CurriculumDto.Create.class) @RequestBody CurriculumDto body) {

        System.out.println(body);
        return curriculumService.createCurriculum(body);
    }

    @GetMapping(value = "{id}")
    public Curriculum getCurriculum(@PathVariable Long id) {
        return curriculumService.getCurriculum(id);
    }

    @PostMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Curriculum uploadCurriculum(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        return parseService.uploadCurriculum(id, file);
    }

    @PostMapping(value = "/{id}/plan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Curriculum addCurriculumPlan(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        return parseService.addCurriculumPlan(id, file);
    }

    @PostMapping("/{id}/manual")
    public Curriculum uploadManualRequirements(
            @PathVariable Long id,
            @RequestBody RequirementDto[] requirements) {


        return parseService.bindRequirements(id, Arrays.asList(requirements));
    }


    @GetMapping
    public List<Curriculum> getAll() {
        return curriculumService.getAll();
    }

    @DeleteMapping(value = "{id}")
    public void deleteCurriculum(@PathVariable Long id) {
        curriculumService.deleteById(id);
    }


    @GetMapping("/{id}/export")
    public ResponseEntity<Object> exportCurriculum(@PathVariable Long id) throws IOException {

        Curriculum curriculum = curriculumService.getCurriculum(id);
        File spreadsheet = curriculumExportService.buildSpreadsheet(curriculum);
        FileInputStream fileInputStream = new FileInputStream(spreadsheet);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                "Content-Disposition",
                String.format("attachment; filename=\"%s\"", spreadsheet.getName())
        );

        headers.add(
                "Access-Control-Expose-Headers",
                "content-disposition"
        );

        headers.add(
                "Cache-Control",
                "no-cache, no-store, must-revalidate"
        );

        headers.add(
                "Pragma",
                "no-cache"
        );

        headers.add(
                "Expires",
                "0"
        );


        ResponseEntity<Object> responseEntity = ResponseEntity
                .ok().headers(headers)
                .contentLength(spreadsheet.length())
                .contentType(MediaType.parseMediaType("application/txt"))
                .body(new InputStreamResource(fileInputStream));


        return responseEntity;

    }

}
