package com.example.auditor.service.curriculum;

import com.example.auditor.domain.curriculum.Curriculum;
import com.example.auditor.domain.curriculum.Requirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurriculumExportService {

    @Value("${audit.parser.type-prefix}")
    private String requirementTypePrefix;

    @Value("${audit.parser.requirements-row-terminator}")
    private String requirementsRowTerminator;



    public File buildSpreadsheet(Curriculum curriculum) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("requirement");

        int rowNum = 0;
        String currentRequirementType = "";
        for (Requirement requirement : curriculum.getRequirements()) {

            if (requirement.getType() != currentRequirementType) {

                Row categoryRow = sheet.createRow(rowNum);
                rowNum += 1;

                currentRequirementType = requirement.getType();
                Cell categoryCell = categoryRow.createCell(0);
                Font bold = workbook.createFont();
                bold.setBold(true);
                CellStyle categoryStyle = workbook.createCellStyle();
                categoryStyle.setFont(bold);
                categoryCell.setCellStyle(categoryStyle);
                categoryCell.setCellValue(requirementTypePrefix + " " + currentRequirementType);
            }

            Row row = sheet.createRow(rowNum);
            rowNum += 1;

            row.createCell(0).setCellValue(requirement.getName());
            row.createCell(1).setCellValue(requirement.getCredit());
            row.createCell(2).setCellValue(requirement.getPatterns());
            row.createCell(3).setCellValue(requirement.getAntipatterns());
        }

        for (int i=0; i<=3; i++) {

            sheet.autoSizeColumn(i);
        }

        File spreadsheet = new File(
                curriculum.getMajor()
                        + " " + String.valueOf(curriculum.getYear())
                        + ".xlsx"
        );

        FileOutputStream fileOutputStream = new FileOutputStream(spreadsheet);

        workbook.write(fileOutputStream);
        workbook.close();
        fileOutputStream.close();

        return spreadsheet;
    }
}
