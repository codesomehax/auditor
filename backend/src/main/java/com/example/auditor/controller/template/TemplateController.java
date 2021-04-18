package com.example.auditor.controller.template;

import com.example.auditor.domain.template.Template;
import com.example.auditor.dto.TemplateDto;
import com.example.auditor.service.template.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("template")
public class TemplateController {
    private final TemplateService templateService;

    @PostMapping
    public Template createTemplate(@RequestBody TemplateDto body){
        return templateService.createTemplate(body);
    }

    @GetMapping("all")
    public List<Template> getAllTemplates(){
        return templateService.getAllTemplates();
    }

    @GetMapping("{id}")
    public Optional<Template> getTemplate(@PathVariable Long id){
        return templateService.getTemplate(id);
    }


    @PostMapping("{id}")
    public Template editTemplate(@PathVariable Long id, @RequestBody TemplateDto body){
        return templateService.editTemplate(id, body);
    }


    @DeleteMapping(value = "{id}")
    public void deleteTemplate(@PathVariable Long id){
        templateService.deleteById(id);
    }
}
