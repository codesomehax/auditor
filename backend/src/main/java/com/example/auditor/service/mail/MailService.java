package com.example.auditor.service.mail;

import com.example.auditor.dto.RegisterDto;
import com.example.auditor.dto.SendMailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    @Value("${smtp.host}")
    private String smtpHost;

    @Value("${smtp.port}")
    private String smtpPort;

    @Value("${smtp.auth}")
    private String smtpAuth;

    @Value("${smtp.tls}")
    private String smtpTls;

    public void sendMail(SendMailDto[] mails) throws RuntimeException {
        String userMail = System.getProperty("userMail");
        String password = System.getProperty("mailPassword");

        if (userMail == null || password == null) {
            throw new RuntimeException("user not registered");
        }

        MailSender mailSender = new MailSender(
                mails,
                userMail,
                password,
                smtpHost,
                smtpPort,
                smtpAuth,
                smtpTls
        );
        mailSender.start();
    }

    public void register(RegisterDto credentials) throws RuntimeException {
        System.setProperty("userMail", credentials.getUserMail());
        System.setProperty("mailPassword", credentials.getPassword());
    }

    public void dropCurrentMailSession() {
        System.clearProperty("userMail");
        System.clearProperty("mailPassword");
    }
}


