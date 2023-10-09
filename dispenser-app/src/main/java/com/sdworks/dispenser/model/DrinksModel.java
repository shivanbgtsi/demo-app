package com.sdworks.dispenser.model;

public class DrinksModel {

    private final String drinkCode;
    private final String drinkName;
    private final int noOfItems;

    private final Double remainingAmount;

    public DrinksModel(String drinkCode, String drinkName, int noOfItems, Double remainingAmount) {
        this.drinkCode = drinkCode;
        this.drinkName = drinkName;
        this.noOfItems = noOfItems;
        this.remainingAmount = remainingAmount;
    }

    public String getDrinkCode() {
        return drinkCode;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    @Override
    public String toString() {
        return "Drinks{" +
                "drinkCode='" + drinkCode + '\'' +
                ", drinkName='" + drinkName + '\'' +
                ", noOfItems=" + noOfItems +
                ", remainingAmount=" + remainingAmount +
                '}';
    }
}
