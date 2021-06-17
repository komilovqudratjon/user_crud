package com.example.backent.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {
  @Autowired private JavaMailSender sender;

  @Autowired private Configuration config;

  public void sendEmail(
      String name, String to, String from, String subject, String user, String ip, String pass) {
    try {

      MimeMessage message = sender.createMimeMessage();
      MimeMessageHelper helper =
          new MimeMessageHelper(
              message,
              MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());
      Template t = config.getTemplate("email.ftl");

      Map<String, String> mail = new HashMap<>();
      mail.put("name", name);
      mail.put("user", user);
      mail.put("to", to);
      mail.put("ip", ip);
      mail.put("pass", pass);

      String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail);
      helper.setTo(to);
      helper.setText(html, true);
      helper.setSubject(subject);
      helper.setFrom(from);
      sender.send(message);
      System.out.println("Email ushbu pochtaga jo'natildi: ");
    } catch (MessagingException | IOException | TemplateException e) {
      System.out.println("Email yuborishda xatolik bo'ldi : ");
    }
  }
}
