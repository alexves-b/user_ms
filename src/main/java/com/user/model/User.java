package com.user.model;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", isDeleted=" + isDeleted +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", authorities=" + authorities +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public User(boolean isDeleted, String firstName, String lastName, String email, String password,
                List <RoleEntity> roles,List<AuthorityEntity> authorities) {
        this.isDeleted = isDeleted;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    boolean isDeleted;
    String firstName;
    String lastName;
    String email;
    String password;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List <RoleEntity> roles;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List <AuthorityEntity> authorities;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
