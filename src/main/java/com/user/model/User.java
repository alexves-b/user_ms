package com.user.model;
import com.user.dto.secure.AccountSecureDto;
import lombok.*;
import javax.persistence.*;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    public User(AccountSecureDto accountSecureDto) {
        this.firstName = accountSecureDto.getFirstName();
        this.lastName = accountSecureDto.getLastName();
        this.email = accountSecureDto.getEmail();
        this.password = accountSecureDto.getPassword();
    }

    Boolean isDeleted;
    String firstName;
    String lastName;
    @Column(name = "email", unique = true)
    String email;
    @Column(columnDefinition = "VARCHAR(768) CHARACTER SET utf8")
    String password;
    String roles;

}
