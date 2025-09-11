package com.jeisoneccel.my_finances.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.error("{}: ", ex.toString(), ex);
        ex.printStackTrace();

        String exceptionType = ex.getClass().getTypeName().toUpperCase();
        String error = ex.getLocalizedMessage();

        RequestError requestError = new RequestError(INTERNAL_SERVER_ERROR, exceptionType, error);
        return new ResponseEntity<>(requestError, new HttpHeaders(), requestError.getHttpStatus());
    }

}
