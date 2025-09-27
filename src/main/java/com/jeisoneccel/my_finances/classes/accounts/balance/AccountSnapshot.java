package com.jeisoneccel.my_finances.classes.accounts.balance;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jeisoneccel.my_finances.classes.accounts.Account;
import com.jeisoneccel.my_finances.classes.accounts.AccountCustomSerializer;
import com.jeisoneccel.my_finances.core.entities.AbstractEntity;
import com.jeisoneccel.my_finances.utils.annotations.NotUpdatable;
import com.jeisoneccel.my_finances.utils.validations.ValidEntityWithNestedField;
import com.jeisoneccel.my_finances.utils.validations.ValidNestedEntity;
import com.jeisoneccel.my_finances.utils.validations.ValidNotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

@Entity
@Table(name = "accounts_balance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidEntityWithNestedField
public class AccountSnapshot extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotUpdatable
    @ValidNotNull
    @ManyToOne
    @ValidNestedEntity
    @JsonSerialize(using = AccountCustomSerializer.class)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;

    @ValidNotNull
    @Column(name = "year_month", columnDefinition = "int", nullable = false, updatable = false)
    int yearMonth;

    @ValidNotNull
    @Column(name = "balance", columnDefinition = "numeric(18,6)", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Override
    public String toString() {
        return "AccountSnapshot{" +
                "id=" + getId() +
                ", account=" + account +
                ", yearMonth=" + yearMonth +
                ", balance=" + balance +
                ", createdDate=" + getCreatedDate() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountSnapshot that)) return false;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getAccount(), that.getAccount()) &&
                getYearMonth() == that.getYearMonth() &&
                Objects.equals(getBalance(), that.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAccount(), getYearMonth(), getBalance());
    }

    public YearMonth convertYearMonth() {
        int year = yearMonth / 100;
        int month = yearMonth % 100;

        return YearMonth.of(year, month);
    }

}
