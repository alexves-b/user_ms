package com.user.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.user.dto.secure.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
    String id;
    Boolean isDeleted;
    String firstName;
    String lastName;
    String email;
    String password;
    String role;
    Authority authority;
    String phone;
    String photo;
    String profileCover;
    String about;
    String city;
    String country;

    String gender;
    StatusCodeType statusCode;
    LocalDateTime regDate;
    LocalDateTime birthDate;
    String messagePermission;
    LocalDateTime lastOnlineTime;
    boolean isOnline;
    boolean isBlocked;
    String emojiStatus;
    LocalDateTime createdOn;
    LocalDateTime updatedOn;
    LocalDateTime deletionDate;
}
