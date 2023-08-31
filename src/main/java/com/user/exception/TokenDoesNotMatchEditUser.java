package com.user.exception;

public class TokenDoesNotMatchEditUser extends RuntimeException {
    public TokenDoesNotMatchEditUser(String message) {
        super(message);
    }
}
