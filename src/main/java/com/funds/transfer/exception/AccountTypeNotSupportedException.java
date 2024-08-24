package com.funds.transfer.exception;

public class AccountTypeNotSupportedException extends RuntimeException {

    public AccountTypeNotSupportedException(String message) {
        super(message);
    }

    public AccountTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
