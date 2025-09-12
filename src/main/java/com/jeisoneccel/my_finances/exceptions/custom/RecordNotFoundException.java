package com.jeisoneccel.my_finances.exceptions.custom;

import com.jeisoneccel.my_finances.exceptions.ErrorCode;

import static com.jeisoneccel.my_finances.exceptions.ErrorCode.ERR0A00001;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(ErrorCode errorCode) {
        super(errorCode.name());
    }

    public RecordNotFoundException(String entity) {
        super(ERR0A00001.withEntity(entity).name());
    }

    public RecordNotFoundException(String entity, String fieldName) {
        super(ERR0A00001.withEntity(entity).withFieldName(fieldName).name());
    }

}
