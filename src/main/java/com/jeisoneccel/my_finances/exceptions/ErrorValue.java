package com.jeisoneccel.my_finances.exceptions;

public record ErrorValue(String entity, String fieldName, ErrorCode code, String message) {

}