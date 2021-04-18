package com.example.auditor.service.template;

import com.example.auditor.domain.template.Template;
import com.example.auditor.dto.TemplateDto;
import com.example.auditor.repository.template.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public Template createTemplate(TemplateDto dto){
        return templateRepository.save(Template.builder()
                .id(null)
                .topic(dto.getTopic())
                .body(dto.getBody())
                .build());
    }

    public Template editTemplate(Long id, TemplateDto dto){
        return templateRepository.save(Template.builder()
                .id(id)
                .topic(dto.getTopic())
                .body(dto.getBody())
                .build());
    }

    public List<Template> getAllTemplates(){
        return templateRepository.findAll();
    }

    public Optional<Template> getTemplate(Long id){
        return templateRepository.findById(id);
    }


    public void deleteById(Long id){
        templateRepository.deleteById(id);
    }
}
