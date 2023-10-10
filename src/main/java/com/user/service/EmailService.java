package com.user.service;

import java.util.UUID;

public interface EmailService {

    void emailConfirmmationWhehRegistered(String email, String uuid);

    void confirmationForChangeEmail(String presentEmail, String futureEmail, UUID uuid,Integer code);

    void sendSimpleMessage(String to, String subject, String text);

}
