package com.internly.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final ObjectProvider<JavaMailSender> sender;
  private final String host;

  public EmailService(
    ObjectProvider<JavaMailSender> sender,
    @Value("${spring.mail.host:}") String host
  ) {
    this.sender = sender;
    this.host = host;
  }

  public void sendVerificationCode(String email, String code) {
    JavaMailSender mailSender = sender.getIfAvailable();
    if (mailSender == null || host.isBlank()) return;
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Verify your Internly account");
    message.setText(
      "Your Internly verification code is " +
        code +
        ". It expires in 10 minutes. If you did not request this, ignore this email."
    );
    mailSender.send(message);
  }

  public void sendPasswordResetCode(String email, String code) {
    JavaMailSender mailSender = sender.getIfAvailable();
    if (mailSender == null || host.isBlank()) return;
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Reset your Internly password");
    message.setText(
      "Your Internly password reset code is " +
        code +
        ". It expires in 10 minutes. If you did not request this, ignore this email."
    );
    mailSender.send(message);
  }
}
