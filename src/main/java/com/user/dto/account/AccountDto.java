package com.user.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.user.model.User;
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
public class AccountDto {
    public AccountDto(User user) {
        this.id = user.getId();
        this.isDeleted = user.getIsDeleted();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRoles();
        this.authority = user.getAuthority();
        this.phone = user.getPhone();
        this.photo = user.getPhoto();
        this.profileCover = user.getProfileCover();
        this.about = user.getAbout();
        this.city = user.getCity();
        this.country = user.getCountry();
        this.gender = user.getGender();
        this.statusCode = user.getStatusCode();
        this.regDate = user.getRegDate();
        this.birthDate = user.getBirthDate();
        this.messagePermission = user.getMessagePermission();
        this.lastOnlineTime = user.getLastOnlineTime();
        this.isOnline = user.getIsOnline();
        this.isBlocked = user.getIsBlocked();
        this.emojiStatus = user.getEmojiStatus();
        this.createdOn = user.getCreatedOn();
        this.updatedOn = user.getUpdatedOn();
        this.deletionDate = user.getDeletionDate();
    }

    Long id;
    Boolean isDeleted;
    String firstName;
    String lastName;
    String email;
    String password;
    String role;
    String authority;
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
