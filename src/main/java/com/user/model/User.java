package com.user.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.user.dto.account.AccountDto;
import com.user.dto.account.StatusCodeType;
import com.user.dto.secure.AccountSecureDto;
import lombok.*;
import javax.persistence.*;
import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    Boolean isDeleted;
    String firstName;
    String lastName;
    @Column(name = "email", unique = true)
    String email;
    @Column(columnDefinition = "VARCHAR(768) CHARACTER SET utf8")
    String password;
    String roles;
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
    Boolean isOnline;
    Boolean isBlocked;
    String emojiStatus;
    LocalDateTime createdOn;
    LocalDateTime updatedOn;
    LocalDateTime deletionDate;
}
