package com.example.auditor.controller.mail;

import com.example.auditor.dto.RegisterDto;
import com.example.auditor.dto.SendMailDto;
import com.example.auditor.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("mail")
public class MailController {


    private final MailService mailService;

    @PostMapping("send")
    public String sendMail(@RequestBody SendMailDto[] mails) throws RuntimeException {
        if (Limiter.get().tryConsume(1)) {
            mailService.sendMail(mails);
            return "successfully dispatched process";
        }
        return "please wait !!!";
    }

    @PostMapping("register")
    public String register(
            @Validated(RegisterDto.Create.class)
            @RequestBody RegisterDto credentials) throws RuntimeException {
        mailService.register(credentials);
        return "successfully registered " + credentials.getUserMail();
    }

    @PostMapping("logout")
    public String logout() {
        mailService.dropCurrentMailSession();
        return "successfully dropped";
    }
}
