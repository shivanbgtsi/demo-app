package com.sdworks.dispenser.model;

public class ProductModel {

    private final String drinkCode;
    private final String drinkName;
    private final Double productPrice;
    private final int maxLimit;
    private final boolean isAvailable;

    private final int availableLimit;

    public ProductModel(String drinkCode, String productName, Double productPrice, int maxLimit, boolean isAvailable,
                        int availableLimit) {
        this.drinkCode = drinkCode;
        this.drinkName = productName;
        this.productPrice = productPrice;
        this.maxLimit = maxLimit;
        this.isAvailable = isAvailable;
        this.availableLimit = availableLimit;
    }

    public String getDrinkCode() {
        return drinkCode;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getAvailableLimit() {
        return availableLimit;
    }
}
