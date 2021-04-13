package com.example.auditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMailDto {
    private String[] studentMails;
    private String message; // TODO: should be template id
}
