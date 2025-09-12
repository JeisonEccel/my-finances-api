package com.jeisoneccel.my_finances.exceptions.custom;

import com.jeisoneccel.my_finances.exceptions.ErrorCode;

import static com.jeisoneccel.my_finances.exceptions.ErrorCode.ERR0A00002;

public class RecordAlreadyExistsException extends RuntimeException {

    public RecordAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode.name());
    }

    public RecordAlreadyExistsException(String entity, String fieldName) {
        super(ERR0A00002.withEntity(entity).withFieldName(fieldName).name());
    }

}
