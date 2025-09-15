package com.jeisoneccel.my_finances.classes.users;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class UserModel {

    private String name;
    private String email;
    private String password;

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel userModel)) return false;
        return Objects.equals(getName(), userModel.getName()) &&
                Objects.equals(getEmail(), userModel.getEmail()) &&
                Objects.equals(getPassword(), userModel.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getPassword());
    }
}
