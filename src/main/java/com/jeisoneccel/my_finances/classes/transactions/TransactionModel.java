package com.jeisoneccel.my_finances.classes.transactions;

import com.jeisoneccel.my_finances.classes.accounts.Account;
import com.jeisoneccel.my_finances.classes.categories.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class TransactionModel {

    private String description;
    private BigDecimal amount = BigDecimal.ZERO;
    private Account account;
    private Category category;

    @Override
    public String toString() {
        return "TransactionModel{" +
                "description='" + description + '\'' +
                ", amount=" + amount +
                ", account=" + account +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionModel that)) return false;
        return Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getAccount(), that.getAccount()) &&
                Objects.equals(getCategory(), that.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getAmount(), getAccount(), getCategory());
    }

}
