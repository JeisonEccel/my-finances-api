package com.jeisoneccel.my_finances.exceptions;

import com.jeisoneccel.my_finances.exceptions.custom.RecordAlreadyExistsException;
import com.jeisoneccel.my_finances.exceptions.custom.RecordNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.jeisoneccel.my_finances.exceptions.ExceptionType.*;
import static org.springframework.http.HttpStatus.*;

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

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentials(Exception ex, HttpServletRequest request) {
        String error = ex.getLocalizedMessage();
        log.info(
                "Bad Credentials: Host: {} | Address: {} | Port: {}",
                request.getRemoteHost(), request.getRemoteAddr(), request.getRemotePort()
        );

        RequestError requestError = new RequestError(UNAUTHORIZED, BAD_CREDENTIALS, error);
        return new ResponseEntity<>(requestError, new HttpHeaders(), requestError.getHttpStatus());
    }

    @ExceptionHandler({RecordNotFoundException.class})
    public ResponseEntity<Object> handleRecordNotFound(Exception ex, WebRequest request) {
        String error = ex.getLocalizedMessage();

        RequestError requestError = new RequestError(NOT_FOUND, RECORD_NOT_FOUND, error);
        return new ResponseEntity<>(requestError, new HttpHeaders(), requestError.getHttpStatus());
    }

    @ExceptionHandler({RecordAlreadyExistsException.class})
    public ResponseEntity<Object> handleRecordAlreadyExists(Exception ex, WebRequest request) {
        String error = ex.getLocalizedMessage();

        RequestError requestError = new RequestError(UNPROCESSABLE_ENTITY, RECORD_ALREADY_EXISTS, error);
        return new ResponseEntity<>(requestError, new HttpHeaders(), requestError.getHttpStatus());
    }

}
