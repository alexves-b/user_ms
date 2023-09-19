package com.user.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountForFriends {

    Long id;
    Boolean isDeleted;
    String firstName;
    String lastName;
    String email;
    String photo;
    String city;
    String country;
    Boolean isBlocked;
    LocalDateTime birthDate;
}
