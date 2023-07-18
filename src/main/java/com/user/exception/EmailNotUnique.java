package com.user.exception;

public class EmailNotUnique extends RuntimeException{
    public EmailNotUnique(String message) {
        super(message);
    }
}
