package com.user.dto.account;

import com.user.dto.secure.Authority;
import com.user.dto.secure.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class AccountDto {
    public AccountDto(String id, boolean isDeleted, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    String id;
    boolean isDeleted;
    String firstName;
    String lastName;
    String email;
    String password;
    Role role;
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
    Timestamp deletionTimestamp;
}
