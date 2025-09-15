package com.jeisoneccel.my_finances.auth.refresh_tokens;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.entities.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    @Column(name = "token", columnDefinition = "text", nullable = false)
    private String token;

    @Column(name = "device", columnDefinition = "varchar(255)", nullable = false)
    private String device;

    @Column(name = "ip_address", columnDefinition = "varchar(64)", nullable = false)
    private String ipAddress;

    @Column(name = "expires_at", columnDefinition = "timestamp WITH TIME ZONE", nullable = false)
    private ZonedDateTime expiresAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken that)) return false;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getToken(), that.getToken()) &&
                Objects.equals(getDevice(), that.getDevice()) &&
                Objects.equals(getIpAddress(), that.getIpAddress()) &&
                Objects.equals(getExpiresAt(), that.getExpiresAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken(), getDevice(), getIpAddress(), getExpiresAt());
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id='" + getId() + '\'' +
                ", token='" + token + '\'' +
                ", device='" + device + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", expiresAt=" + expiresAt +
                ", createdDate=" + getCreatedDate() +
                '}';
    }
}
