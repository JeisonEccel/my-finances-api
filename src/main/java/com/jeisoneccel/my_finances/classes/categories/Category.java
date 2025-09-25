package com.jeisoneccel.my_finances.classes.categories;

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
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = {@UniqueConstraint(name = "category_owner", columnNames = "owner, name")}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidEntityWithNestedField
public class Category extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ValidString
    @ValidNotBlank
    @StringMax128
    @Column(name = "name", columnDefinition = "varchar(128)", nullable = false)
    private String name;

    @NotUpdatable
    @ValidNotNull
    @ManyToOne
    @ValidNestedEntity
    @JsonSerialize(using = UserCustomSerializer.class)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User owner;

    @ValidNotNull
    @Column(name = "goal", columnDefinition = "numeric(18,6)", nullable = false)
    private BigDecimal goal = BigDecimal.ZERO;

    @Override
    public String toString() {
        return "Category{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", goal=" + goal +
                ", createdDate=" + getCreatedDate() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(getId(), category.getId()) &&
                Objects.equals(getName(), category.getName()) &&
                Objects.equals(getOwner(), category.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getOwner());
    }

}
