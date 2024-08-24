package com.funds.transfer.exception;

public class TransactionTypeNotSupportedException extends RuntimeException {

    public TransactionTypeNotSupportedException(String message) {
        super(message);
    }

    public TransactionTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
