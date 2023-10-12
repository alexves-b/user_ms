package com.user.exception;

public class ConfirmationCodeNotCorrect extends RuntimeException {
    public ConfirmationCodeNotCorrect(String message) {
        super(message);
    }
}

