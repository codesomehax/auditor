package com.example.auditor.service.curriculum;

import com.example.auditor.domain.curriculum.Curriculum;
import com.example.auditor.domain.curriculum.Requirement;
import com.example.auditor.dto.RequirementDto;
import com.example.auditor.repository.curriculum.CurriculumRepository;
import com.example.auditor.repository.curriculum.RequirementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurriculumUploadService {

    @Value("${audit.parser.type-prefix}")
    private String requirementTypePrefix;

    @Value("${audit.parser.requirements-row-terminator}")
    private String requirementsRowTerminator;

    @Value("${audit.parser.curriculum-ectc-notation}")
    private Boolean ectcMode;

    private final CurriculumRepository curriculumRepository;

    private final RequirementRepository requirementRepository;

    public Curriculum uploadCurriculum(Long id, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curriculum file must not be null or empty");
        }

        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find curriculum with specified id"));

        List<Requirement> requirements = new LinkedList<>();

        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = wb.getSheetAt(0);
            String lastRequirementType = null;

            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                Cell firstColumnCell = sheet.getRow(i).getCell(0);

                String firstColumnValue = validateCellAndGetString(firstColumnCell, i);

                if (firstColumnValue.startsWith(requirementTypePrefix)) {
                    int prefixLength = requirementTypePrefix.length();
                    lastRequirementType = firstColumnValue.substring(prefixLength).strip();
                } else if (lastRequirementType == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requirements must be annotated by at least one category");
                } else if (firstColumnValue.startsWith(requirementsRowTerminator)) {
                    break;
                } else {

                    Cell secondColumnCell = sheet.getRow(i).getCell(1);
                    Integer credit = validateCellAndGetInteger(secondColumnCell, i);

                    String patterns;
                    Cell patternsCell = sheet.getRow(i).getCell(2);
                    if (patternsCell == null || patternsCell.getCellType() == CellType.BLANK) {
                        patterns = firstColumnValue.strip();
                        patterns = patterns.replaceAll("\\p{Pd}", "-");
                        if (patterns.contains("-")) {
                            patterns = patterns.split("-")[0].strip();
                        }
                    } else {
                        patterns = patternsCell.getStringCellValue().strip();
                    }


                    Requirement requirement = requirementRepository.save(Requirement.builder()
                            .credit(ectcMode ? credit * 2 : credit)
                            .patterns(patterns)
                            .name(firstColumnValue)
                            .type(lastRequirementType)
                            .build());
                    log.info("requirement: " + requirement);
                    requirements.add(requirement);

                }
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        curriculum.setRequirements(requirements);
        return curriculumRepository.save(curriculum);
    }


    public Curriculum bindRequirements(Long id, List<RequirementDto> dtos) {

        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find curriculum with specified id"));

        List<Requirement> requirements = new LinkedList<>();

        for (RequirementDto dto : dtos) {

            Requirement requirement = requirementRepository.save(
                    Requirement.builder()
                        .credit(ectcMode ? dto.getCredit() * 2 : dto.getCredit())
                        .name(dto.getName())
                        .type(dto.getType())
                    .build()
            );

            System.out.println(requirement);

            requirements.add(requirement);
        }

        curriculum.setRequirements(requirements);
        return curriculumRepository.save(curriculum);
    }


    private String validateCellAndGetString(Cell cell, int i) {
        if (cell == null || cell.getCellType() != CellType.STRING) {
            String message = "Expected String type of cell at row: "
                    + (i + 1) + "but found ";

            message += cell == null
                    ? "null"
                    : cell.getCellType().name();

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        } else {
            return cell.getStringCellValue();
        }

    }

    private Integer validateCellAndGetInteger(Cell cell, int i) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            String message = "Expected Integer type of cell at row: "
                    + (i + 1) + "but found ";

            message += cell == null
                    ? "null"
                    : cell.getCellType().name();

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        try {
            return (Integer) (int) cell.getNumericCellValue();
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected Integer type of cell at row: " + (i + 1));
        }
    }

}
