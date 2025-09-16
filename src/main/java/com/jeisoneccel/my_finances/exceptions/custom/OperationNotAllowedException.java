package com.jeisoneccel.my_finances.exceptions.custom;

import com.jeisoneccel.my_finances.exceptions.ErrorCode;

public class OperationNotAllowedException extends RuntimeException {

    public OperationNotAllowedException(ErrorCode errorCode) {
        super(errorCode.name());
    }

}
