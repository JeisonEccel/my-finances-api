package com.jeisoneccel.my_finances.exceptions;

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

import static com.jeisoneccel.my_finances.exceptions.ExceptionType.BAD_CREDENTIALS;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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

}
