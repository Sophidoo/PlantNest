package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.service.MailingService;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailingServiceImpl implements MailingService {

    @Autowired
    private TemplateHelperImpl templateHelper;
    @Autowired
    private JavaMailSender emailSender;
    @SneakyThrows
    @Override
    public void sendMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(text, true); //
        helper.setTo(to);
        helper.setSubject(subject);
        emailSender.send(mimeMessage);
    }

    @Override
    public void sendMessageWithTemplate(String to, String subject, String template, Object context) throws IOException {
        String text = templateHelper.compile(template,context);
        sendMessage(to, subject, text);
    }
}
