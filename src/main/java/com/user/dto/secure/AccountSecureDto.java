package com.user.dto.secure;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class AccountSecureDto {
    public AccountSecureDto(String firstName, String lastName, String email, String password) {
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
    String roles;
}
