package com.user.dto;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationCode {
    public ConfirmationCode() {
        this.code = (int) (Math.random() *10000);
    }

    @Override
    public String toString() {
        return "" + code;
    }

    int code;


}
