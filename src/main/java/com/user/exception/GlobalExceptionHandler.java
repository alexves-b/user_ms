package com.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchEmailNotFoundException(EmailNotUnique e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationError(HttpStatus.CONFLICT
                .value(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchEmailNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationError(HttpStatus.NOT_FOUND
                .value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }
}