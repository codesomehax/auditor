package com.example.auditor.controller.curriculum;

import com.example.auditor.domain.curriculum.Curriculum;
import com.example.auditor.dto.CurriculumDto;
import com.example.auditor.dto.RequirementDto;
import com.example.auditor.service.curriculum.CurriculumService;
import com.example.auditor.service.curriculum.CurriculumUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("curriculum")
public class CurriculumController {

    private final CurriculumUploadService parseService;
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

}
