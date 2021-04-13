package com.example.auditor.service.mail;

import com.example.auditor.dto.SendMailDto;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
class MailSender implements Runnable {
    private Thread t;
    private SendMailDto[] mails;
    private String username;
    private String password;

    private String smtpHost;
    private String smtpPort;
    private String smtpAuth;
    private String smtpTls;


    MailSender(SendMailDto[] mails,
               String userMail,
               String password,
               String host,
               String port,
               String auth,
               String tls) {
        this.mails = mails;
        this.username = userMail;
        this.password = password;
        smtpHost = host;
        smtpPort = port;
        smtpAuth = auth;
        smtpTls = tls;
    }

    public void sendMail(String[] studentAddresses, String template) throws Exception {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", smtpHost);
        prop.put("mail.smtp.port", smtpPort);
        prop.put("mail.smtp.auth", smtpAuth);
        prop.put("mail.smtp.starttls.enable", smtpTls); //TLS


        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        log.info("Trying to send the email with user " + username);

        String addresses = Arrays.stream(studentAddresses).reduce("", (sub, el) -> sub + ", " + el);

        if (addresses.length() == 0) {
            throw new RuntimeException("No Student address was found!");
        }

        addresses = addresses.substring(1).trim(); /* remove initial comma */
        log.info("Generated addresses " + addresses);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(addresses)
        );
        message.setSubject("Review of your performance");
        message.setText("Dear Student,\n\n" + template);

        Transport.send(message);
        log.info("Done");

    }

    public void run() {
        for (SendMailDto mail : mails) {
            try {
                sendMail(mail.getStudentMails(), mail.getMessage());
                Thread.sleep(5000);
            } catch (Exception e) {
                log.error("Failed to send message!");
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
