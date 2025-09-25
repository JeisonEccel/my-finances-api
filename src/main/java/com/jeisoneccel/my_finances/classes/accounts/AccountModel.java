package com.jeisoneccel.my_finances.classes.accounts;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class AccountModel {

    private String name;

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountModel userModel)) return false;
        return Objects.equals(getName(), userModel.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

}
