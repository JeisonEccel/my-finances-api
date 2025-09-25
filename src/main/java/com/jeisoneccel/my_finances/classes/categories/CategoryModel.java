package com.jeisoneccel.my_finances.classes.categories;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class CategoryModel {

    private String name;
    private BigDecimal goal = BigDecimal.ZERO;

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                "goal=" + goal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryModel userModel)) return false;
        return Objects.equals(getName(), userModel.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

}
