package com.jeisoneccel.my_finances.core.entities;

import jakarta.validation.*;

import java.time.ZonedDateTime;
import java.util.Set;

public interface BasicEntity {

    String getId();

    ZonedDateTime getCreatedDate();

    default void validateSchema() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<BasicEntity>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}