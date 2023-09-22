package com.user.dto.secure;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Builder
public class AccountSecureDto {

    public AccountSecureDto(Long id, String firstName, String lastName, String email, String password, String roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
    Long id;
    String firstName;
    String lastName;
    String email;
    String password;
    String roles;
}
