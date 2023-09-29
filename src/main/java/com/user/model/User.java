package com.user.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.user.dto.account.AccountDto;
import com.user.dto.account.StatusCodeType;
import com.user.dto.secure.AccountSecureDto;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;
import reactor.util.annotation.Nullable;

import javax.persistence.*;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

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

    UUID uuidConfirmationEmail;
    Boolean isConfirmed = false;
    StatusCodeType statusCode;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime regDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime birthDate;
    String messagePermission;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime lastOnlineTime;
    Boolean isOnline;
    Boolean isBlocked;
    String emojiStatus;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdOn;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime updatedOn;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime deletionDate;
    @Nullable
    LocalDateTime dateToConfirmation;
}
