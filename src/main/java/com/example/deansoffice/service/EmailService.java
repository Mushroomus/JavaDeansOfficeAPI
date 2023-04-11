package com.example.deansoffice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(String toEmail, String subject, Map<String, Object> model, String template) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

        helper.setFrom("DeansOffice@example.pl");
        helper.setTo(toEmail);
        helper.setSubject(subject);

        Context context = new Context();
        context.setVariables(model);
        String html = templateEngine.process(template, context);

        mimeMessage.setContent(html, "text/html");
        javaMailSender.send(mimeMessage);
    }
}