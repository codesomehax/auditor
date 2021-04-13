package com.example.auditor.config;

import lombok.SneakyThrows;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ParserConfig {

    @Bean
    @Scope(value = "prototype")
    @SneakyThrows
    PDFTextStripper pdfTextStripper() {
        return new PDFTextStripper();
    }

}
