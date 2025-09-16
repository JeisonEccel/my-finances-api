package com.jeisoneccel.my_finances.classes.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeisoneccel.my_finances.core.entities.AbstractEntity;
import com.jeisoneccel.my_finances.utils.annotations.IgnoreTrim;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Objects;

import static com.jeisoneccel.my_finances.classes.users.UserDefaults.DEFAULT_USER_EMAIL_VERIFIED;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(name = "user_email", columnNames = "email")}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "name", columnDefinition = "varchar(128)", nullable = false)
    private String name;

    @Column(name = "email", columnDefinition = "varchar(255)", nullable = false, unique = true)
    private String email;

    @IgnoreTrim
    @Column(name = "password", columnDefinition = "text", nullable = false)
    private String password;

    @Column(name = "email_verified", columnDefinition = "boolean default false", nullable = false)
    private boolean emailVerified = DEFAULT_USER_EMAIL_VERIFIED;

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", emailVerified=" + emailVerified +
                ", createdDate=" + getCreatedDate() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getEmail(), user.getEmail());
    }

}
