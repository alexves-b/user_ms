package com.user.jwt_token;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private Long userId;

    @Getter
    private String firstName;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Getter
    private String lastName;

    public Long getId() {
        return userId;
    }

    public void setId(Long id) {
        this.userId = id;
    }

    public CustomUserDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Long userId,
            String firstName,
            String lastName) {
        super(username, password, authorities);
        setId(userId);
        setFirstName(firstName);
        setLastName(lastName);
    }
}

