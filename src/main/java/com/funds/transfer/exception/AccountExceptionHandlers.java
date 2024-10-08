package com.funds.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class AccountExceptionHandlers {

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<Object> accountNotFoundException(AccountNotFoundException e) {
        APIException apiException = new APIException(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    @ExceptionHandler(value = AccountTypeNotSupportedException.class)
    public ResponseEntity<Object> accountTypeNotSupportedException(AccountTypeNotSupportedException e) {
        APIException apiException = new APIException(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
