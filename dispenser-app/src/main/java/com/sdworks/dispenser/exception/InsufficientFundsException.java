package com.sdworks.dispenser.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
