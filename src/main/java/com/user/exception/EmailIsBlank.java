package com.user.exception;

public class EmailIsBlank extends RuntimeException{
    public EmailIsBlank(String message) {
        super(message);
    }
}
