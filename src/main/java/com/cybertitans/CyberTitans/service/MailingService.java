package com.cybertitans.CyberTitans.service;

import java.io.IOException;

public interface MailingService {
    void sendMessage(String to, String subject, String text);

    void sendMessageWithTemplate(String to, String subject, String template,Object context) throws IOException;
}
