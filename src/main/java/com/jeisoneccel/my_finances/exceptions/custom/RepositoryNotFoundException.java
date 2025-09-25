package com.jeisoneccel.my_finances.exceptions.custom;

import com.jeisoneccel.my_finances.exceptions.ErrorCode;

public class RepositoryNotFoundException extends RuntimeException {

    public RepositoryNotFoundException(ErrorCode errorCode) {
        super(errorCode.name());
    }

}