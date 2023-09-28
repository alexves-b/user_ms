package com.user.service;

public interface EmailService {

    void emailConfirmmationWhehRegistered(String email, String uuid);

    void confirmationForChangeEmail();

    void sendSimpleMessage(String to, String subject, String text);

}
