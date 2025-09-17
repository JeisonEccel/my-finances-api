package com.jeisoneccel.my_finances.utils.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import org.hibernate.validator.constraints.Length;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Length(max = 4000)
@NotPattern(regexp = ".*[{}\\[\\]].*")
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@ReportAsSingleViolation
public @interface ValidString {

    String message() default "ERR0V00007";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
