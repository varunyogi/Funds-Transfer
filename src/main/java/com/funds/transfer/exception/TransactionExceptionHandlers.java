package com.funds.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class TransactionExceptionHandlers {

    @ExceptionHandler(value = InsufficientAmountException.class)
    public ResponseEntity<Object> insufficientAmountException(InsufficientAmountException e) {
        APIException apiException = new APIException(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    @ExceptionHandler(value = CurrencyNotSupportedException.class)
    public ResponseEntity<Object> currencyNotSupportedException(CurrencyNotSupportedException e) {
        APIException apiException = new APIException(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
