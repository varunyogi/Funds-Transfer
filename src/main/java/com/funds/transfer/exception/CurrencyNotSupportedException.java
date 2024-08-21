package com.funds.transfer.exception;

public class CurrencyNotSupportedException extends RuntimeException {

    public CurrencyNotSupportedException(String message) {
        super(message);
    }

    public CurrencyNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
