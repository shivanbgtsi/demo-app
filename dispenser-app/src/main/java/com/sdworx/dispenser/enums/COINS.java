package com.sdworx.dispenser.enums;

public enum COINS {
    FIVE_CENTS(0.05d),
    TEN_CENTS(0.10d),
    TWENTY_CENTS(0.20d),
    FIFTY_CENTS(0.50d),
    ONE_DOLLAR(1d),
    TWO_DOLLAR(2d);

    private final double value;

    COINS(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
