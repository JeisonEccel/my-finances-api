package com.jeisoneccel.my_finances.utils.validations;

import com.jeisoneccel.my_finances.utils.validations.validators.NotPatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotPatternValidator.class)
public @interface NotPattern {

    String regexp();

    String message() default "ERR0V00007";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
