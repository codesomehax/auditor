package com.example.auditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableConfigurationProperties
public class AuditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditorApplication.class, args);
    }

}
