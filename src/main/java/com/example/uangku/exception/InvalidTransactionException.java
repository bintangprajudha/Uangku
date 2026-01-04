package com.example.uangku.exception;

/**
 * Exception thrown when a transaction is invalid
 * (e.g., negative amount, missing required fields)
 */
public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message) {
        super(message);
    }

    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
