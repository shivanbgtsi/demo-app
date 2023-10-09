package com.sdworks.dispenser.exception;

public class InsufficientFunds extends RuntimeException {
    public InsufficientFunds(String exceptionMessage) {
        super(exceptionMessage);
    }
}
