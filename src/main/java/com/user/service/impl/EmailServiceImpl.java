package com.user.service.impl;

import com.user.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Override
    public void emailConfirmmationWhehRegistered(String email, String uuid) {
    String text = "Уважаемый пользователь, Ваш емейл "+ email +" был предоставлен для регистрации " +
            "в социальной сети \"Собутыльники\". \n Подтвердите свой емейл перейдя по ссылке: \n " +
            "http://5.63.154.191:8088/users/approve/"+uuid +".\n В случае отсутствия подтверждения емейла, через три дня аккаунт будет удален.";
        sendSimpleMessage(email, "Подтверждение емейла", text);
    }
    @Override
    public void confirmationForChangeEmail() {

    }
    private static final String NOREPLY_ADDRESS = "noreply@socialnet.ru";

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            log.info("message for email " + to +" was send!");
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }
}
