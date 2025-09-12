package com.jeisoneccel.my_finances.exceptions.custom;

import com.jeisoneccel.my_finances.exceptions.ErrorCode;

public class CustomConstraintViolationException extends RuntimeException {

    public CustomConstraintViolationException(ErrorCode errorCode) {
        super(errorCode.name());
    }

}
