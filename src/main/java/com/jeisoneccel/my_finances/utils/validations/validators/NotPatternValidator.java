package com.jeisoneccel.my_finances.utils.validations.validators;

import com.jeisoneccel.my_finances.utils.validations.NotPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotPatternValidator implements ConstraintValidator<NotPattern, String> {

    private String regexp;

    public void initialize(NotPattern constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        return value == null || !value.matches(regexp);
    }

}
