package com.example.auditor.service.curriculum;

import com.example.auditor.domain.curriculum.Curriculum;
import com.example.auditor.dto.CurriculumDto;
import com.example.auditor.repository.curriculum.CurriculumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;

    public Curriculum getCurriculum(Long id) {
        return curriculumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find curriculum with id: " + id));
    }

    public Curriculum createCurriculum(CurriculumDto dto) {
        return curriculumRepository.save(Curriculum
                .builder()
                .id(null)
                .major(dto.getMajor())
                .year(dto.getYear())
                .requirements(new LinkedList<>())
                .build()
        );
    }

    public List<Curriculum> getAll() {
        return curriculumRepository.findAll();
    }

    public void deleteById(Long id) {
        if (curriculumRepository.existsById(id)) {
            curriculumRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid curriculum id");
        }

    }

}
