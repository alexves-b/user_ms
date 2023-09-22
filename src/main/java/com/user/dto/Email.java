package com.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Email {
    public Email(String email) {
        this.email = email;
    }
    String email;
}
