package com.sdworx.dispenser.entity;

import com.sdworx.dispenser.enums.COINS;

import java.util.ArrayList;
import java.util.List;

public class Dispenser {

    private List<COINS> currentTransaction;
    private double sum;
    private double totalRevenue;

    public void insertCoin(COINS coin) {
        if (currentTransaction == null) {
            currentTransaction = new ArrayList<>();
        }
        this.setSum(coin);
        currentTransaction.add(coin);
    }

    public List<COINS> getCurrentTransactionCount() {
        return currentTransaction;
    }

    public void setCurrentTransaction() {
        currentTransaction = null;
        sum = 0;
    }

    public double getSum() {
        return sum;
    }

    private void setSum(COINS coins) {
        this.sum += coins.getValue();
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue += totalRevenue;
    }

}
