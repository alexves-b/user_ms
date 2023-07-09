package com.user.dto.account;
import com.user.dto.secure.Authority;
import com.user.dto.secure.Role;
import lombok.Data;
@Data
public class AccountSecureDto {
    String id;
    boolean isDeleted;
    String firstName;
    String email;
    String password;
    Role role;
    Authority authority;

    public AccountSecureDto(String id, boolean isDeleted, String firstName, String email, String password, Role role, Authority authority) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authority = authority;
    }
}
