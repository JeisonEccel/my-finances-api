package com.jeisoneccel.my_finances.classes.transactions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jeisoneccel.my_finances.classes.accounts.Account;
import com.jeisoneccel.my_finances.classes.accounts.AccountCustomSerializer;
import com.jeisoneccel.my_finances.classes.categories.Category;
import com.jeisoneccel.my_finances.classes.categories.CategoryCustomSerializer;
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
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidEntityWithNestedField
public class Transaction extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotUpdatable
    @ValidNotNull
    @ManyToOne
    @ValidNestedEntity
    @JsonSerialize(using = UserCustomSerializer.class)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User owner;

    @ValidNotNull
    @Column(name = "date", columnDefinition = "date default CURRENT_DATE", nullable = false)
    private LocalDate date = LocalDate.now();

    @ValidString
    @ValidNotBlank
    @StringMax128
    @Column(name = "description", columnDefinition = "varchar(128)", nullable = false)
    private String description;

    @ValidNotNull
    @Column(name = "amount", columnDefinition = "numeric(18,6)", nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @ValidNotNull
    @ManyToOne
    @ValidNestedEntity
    @JsonSerialize(using = AccountCustomSerializer.class)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @ValidNotNull
    @ValidMin1
    @Column(name = "index", columnDefinition = "int default 0", nullable = false)
    private int index = 0;

    @ValidNotNull
    @ManyToOne
    @ValidNestedEntity
    @JsonSerialize(using = CategoryCustomSerializer.class)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + getId() + '\'' +
                ", owner=" + (owner != null ? owner.getName() : "") +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", account=" + (account != null ? account.getName() : "") +
                ", index=" + index +
                ", category=" + (category != null ? category.getName() : "") +
                ", createdDate=" + getCreatedDate() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction transaction)) return false;
        return Objects.equals(getId(), transaction.getId()) &&
                Objects.equals(getOwner(), transaction.getOwner()) &&
                Objects.equals(getDate(), transaction.getDate()) &&
                Objects.equals(getDescription(), transaction.getDescription()) &&
                Objects.equals(getAmount(), transaction.getAmount()) &&
                Objects.equals(getAccount(), transaction.getAccount()) &&
                Objects.equals(getIndex(), transaction.getIndex()) &&
                Objects.equals(getCategory(), transaction.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getOwner(),
                getDate(),
                getDescription(),
                getAmount(),
                getAccount(),
                getIndex(),
                getCategory()
        );
    }

}
