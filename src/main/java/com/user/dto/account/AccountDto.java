package com.user.dto.account;

import com.user.dto.secure.Authority;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class AccountDto {
    public AccountDto(String id, boolean isDeleted, String firstName, String lastName, String email, String password, String role) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

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
