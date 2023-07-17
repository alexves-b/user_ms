package com.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApplicationError> catchEmailNotFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationError(HttpStatus.BAD_REQUEST
                .value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}