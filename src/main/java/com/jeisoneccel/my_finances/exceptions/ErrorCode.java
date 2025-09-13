package com.jeisoneccel.my_finances.exceptions;

public enum ErrorCode {

    // ERR0A -> General Error
    ERR0A00000("Something went wrong"),
    ERR0A00001("Record not found"),
    ERR0A00002("Record already exists"),
    ERR0A00003("Field is not updatable"),
    ERR0A00004("Invalid username or password"),
    ERR0A00005("Missing bearer token"),
    ERR0A00006("Expired bearer token"),
    ERR0A00007("Invalid bearer token"),

    // ERR0V -> Validations
    ERR0V00001("Zoned Date Time format not valid. Value must follow the pattern 'yyyy-MM-ddTHH:mm:ss±zone-offset'"),
    ERR0V00002("Local Date format not valid. Value must follow the pattern 'yyyy-MM-dd'"),
    ERR0V00003("Local Date Time format not valid. Value must follow the pattern 'yyyy-MM-ddTHH:mm:ss'"),
    ERR0V00004("Local Time format not valid. Value must follow the pattern 'HH:mm:ss'");

    public String entity;
    public String fieldName;
    public String message;

    ErrorCode(String defaultMessage) {
        this.message = defaultMessage;
    }

    public static ErrorCode findByName(String name) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.name().equalsIgnoreCase(name)) return errorCode;
        }
        return null;
    }

    public ErrorCode withEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public ErrorCode withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public ErrorCode withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public String getEntity() {
        return this.entity;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
