package com.jeisoneccel.my_finances.utils.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNestedEntity {

    String message() default "ERR0V00015";

    boolean acceptsNullId() default false;

    boolean skipRepositoryLoad() default false;

}
