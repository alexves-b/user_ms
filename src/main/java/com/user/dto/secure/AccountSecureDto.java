package com.user.dto.secure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSecureDto {
    String id;
    boolean isDeleted;
    String firstName;
    String LastName;
    String email;
    String password;
    String role;
    String authority;

}
