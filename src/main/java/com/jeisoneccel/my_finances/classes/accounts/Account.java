package com.jeisoneccel.my_finances.classes.accounts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.classes.users.UserCustomSerializer;
import com.jeisoneccel.my_finances.core.entities.AbstractEntity;
import com.jeisoneccel.my_finances.utils.annotations.NotUpdatable;
import com.jeisoneccel.my_finances.utils.validations.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.Objects;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {@UniqueConstraint(name = "account_owner", columnNames = "owner, name")}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidEntityWithNestedField
public class Account extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotUpdatable
    @ValidNotNull
    @ManyToOne
    @ValidNestedEntity
    @JsonSerialize(using = UserCustomSerializer.class)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User owner;

    @ValidString
    @ValidNotBlank
    @StringMax128
    @Column(name = "name", columnDefinition = "varchar(128)", nullable = false)
    private String name;

    @Override
    public String toString() {
        return "Account{" +
                "id='" + getId() + '\'' +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", createdDate=" + getCreatedDate() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(getId(), account.getId()) &&
                Objects.equals(getOwner(), account.getOwner()) &&
                Objects.equals(getName(), account.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner(), getName());
    }
}
