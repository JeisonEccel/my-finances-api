package com.jeisoneccel.my_finances.core.entities;

import jakarta.validation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface BasicEntity {

    String getId();

    void setId(String id);

    ZonedDateTime getCreatedDate();

    default void validateSchema() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<BasicEntity>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    default List<Field> getAllFields(Class<?> clazz) {
        if (clazz == Object.class) return new ArrayList<>();

        List<Field> declaredFields = Arrays.stream(clazz.getDeclaredFields()).filter(
                field -> !Modifier.isStatic(field.getModifiers())
        ).toList();
        List<Field> superClassFields = getAllFields(clazz.getSuperclass());
        return Stream.concat(declaredFields.stream(), superClassFields.stream()).collect(Collectors.toList());
    }

}