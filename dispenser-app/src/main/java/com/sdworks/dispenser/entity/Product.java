package com.sdworks.dispenser.entity;


import java.util.Objects;

public class Product {

    private final String drinkCode;
    private final String drinkName;
    private final Double productPrice;
    private final int maxLimit;
    private boolean isAvailable;
    private int availableLimit;

    public Product(String drinkCode, String drinkName, Double productPrice, int maxLimit, boolean isAvailable) {
        this.drinkCode = drinkCode;
        this.drinkName = drinkName;
        this.productPrice = productPrice;
        this.maxLimit = maxLimit;
        this.availableLimit = maxLimit;
        this.isAvailable = isAvailable;
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

    public int getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(int availableLimit) {
        this.availableLimit = availableLimit;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product products = (Product) o;
        return Objects.equals(drinkCode, products.drinkCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drinkCode);
    }

    @Override
    public String toString() {
        return "Products{" +
                "drinkCode='" + drinkCode + '\'' +
                ", productName='" + drinkName + '\'' +
                ", productPrice=" + productPrice +
                ", maxLimit=" + maxLimit +
                ", availableLimit=" + availableLimit +
                '}';
    }
}
