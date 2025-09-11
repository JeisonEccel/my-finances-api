package com.jeisoneccel.my_finances.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RequestError {

    private HttpStatus httpStatus;
    private String exceptionType;
    private List<ErrorValue> errors = new ArrayList<>();

    public RequestError(HttpStatus httpStatus, String exceptionType, String error) {
        this.httpStatus = httpStatus;
        this.exceptionType = exceptionType;
        addErrorToList(error);
    }

    private void addErrorToList(String error) {
        ErrorValue value;
        ErrorCode code = ErrorCode.findByName(error);
        if (code != null) {
            value = new ErrorValue(code.getEntity(), code.getFieldName(), code, code.getMessage());
        } else {
            value = new ErrorValue(null, null, null, error);
        }
        this.errors.add(value);
    }

}
