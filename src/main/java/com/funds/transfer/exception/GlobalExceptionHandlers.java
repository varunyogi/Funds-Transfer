package com.funds.transfer.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandlers {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        APIException apiException;

        if (ex.getMessage().contains("[CREDIT, DEBIT]")) {
            String errorMessage = "Our Application only supports account type as CREDIT or DEBIT";
            apiException = new APIException(errorMessage, HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        } else if (ex.getMessage().contains("[TRANSFER, WITHDRAWAL, DEPOSIT]")) {
            String errorMessage = "Our Application only supports transaction types as WITHDRAWAL,DEPOSIT or TRANSFER";
            apiException = new APIException(errorMessage, HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

        } else {
            apiException = new APIException(ex.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        APIException apiException = null;
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String message = violation.getMessage();
            apiException = new APIException(message, HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
        }

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        APIException apiException = null;
        for (ObjectError errors : ex.getBindingResult().getAllErrors()) {
            apiException = new APIException(errors.getDefaultMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
        }
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}



