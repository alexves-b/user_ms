package com.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class RequestDtoChangeEmail {
    public RequestDtoChangeEmail(Email email) {
        this.email = email;
    }

    Email email;
}
