package com.user.service.impl;

import com.user.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
            "в социальной сети \"Собутыльники\". \nПодтвердите свой емейл перейдя по ссылке: \n " +
            "http://5.63.154.191:8088/users/approve/"+uuid +".\n В случае отсутствия подтверждения емейла, через три дня аккаунт будет удален.";
        sendSimpleMessage(email, "Подтверждение емейла", text);
    }
    @Override
    public void confirmationForChangeEmail(String presentEmail, String futureEmail,UUID uuid,Integer code) {
        String textForPresentEmail = "Уважаемый пользователь, Получен запрос на изменение емейла с "
                + presentEmail + "в социальной сети \"Собутыльники\""
                + "Емейл будет изменен с " + presentEmail + " на емейл " + futureEmail
                + "Для подтверждения изменения введите на странице восстановления код: " + code;

        String textForFutureEmail = "Уважаемый пользователь, Вы запросиили изменение емейла \"в социальной сети \\\"Собутыльники\\\" с "+ presentEmail +"."
               + "\nЕмейл будет изменен с " + presentEmail + " на емейл " + futureEmail + "."+
                 "\n Подтвердите действие перейдя по ссылке: \n " +
                "http://5.63.154.191:8088/users/approve/change?uuid=" +uuid +"&futureEmail=" +futureEmail +"&presentEmail=" +presentEmail +".\n В случае отсутствия подтверждения емейла, емейл не будет изменен.";
        sendSimpleMessage(presentEmail, "Подтверждение емейла", textForPresentEmail);
        sendSimpleMessage(futureEmail, "Подтверждение емейла", textForFutureEmail);
    }
    @Value("${spring.mail.username}")
    private String mailServerUsername;
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailServerUsername);
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
