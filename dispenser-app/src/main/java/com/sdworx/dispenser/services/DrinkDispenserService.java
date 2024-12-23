package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Dispenser;
import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.enums.Coin;
import com.sdworx.dispenser.exception.InsufficientFundsException;
import com.sdworx.dispenser.model.DrinkModel;
import com.sdworx.dispenser.model.DrinkResponseModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.sdworx.dispenser.enums.Coin.*;

@Service
@AllArgsConstructor
public class DrinkDispenserService implements ItemDispenser {

    private ProductsService productsService;

    private Dispenser dispenser;

    public DrinkResponseModel dispense(String drinkCode, int noOfUnits) {
        Map<Coin, Integer> coinCounts = dispenser.getCoinCounts();
        double sum = getCurrentTransactionAmount(coinCounts);
        Drink drink = productsService.getProductByDrinkCode(drinkCode);

        validateQuantity(drink.getAvailableQuantity(), noOfUnits);
        double totalAmount = drink.getPrice() * noOfUnits;
        validateCoins(sum, totalAmount);
        drink.setAvailableQuantity(drink.getMaxLimit() - noOfUnits);
        DrinkModel drinkModel = new DrinkModel(drink.getDrinkCode(), drink.getDrinkName(),
                drink.getPrice(), drink.getMaxLimit(), drink.getAvailableQuantity());
        productsService.updateProduct(drinkModel);

        dispenser.setTotalRevenue(dispenser.getTotalRevenue() + totalAmount);
        dispenser.setCoinCounts(new HashMap<>());

        return new DrinkResponseModel(drinkCode, drink.getDrinkName(), noOfUnits,
                getBalanceCoins(sum - totalAmount));
    }

    public Set<DrinkModel> getProducts() {
        return productsService.getProducts();
    }

    private List<Coin> getBalanceCoins(Double balanceAmount) {
        if (balanceAmount == 0) {
            return List.of();
        }
        List<Double> coinsList = Arrays.stream(Coin.values())
                .map(Coin::getValue)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (coinsList.contains(balanceAmount)) {
            return List.of(getCoins(balanceAmount));
        }

        List<Coin> lists = new ArrayList<>();
        int i = 0;
        for (; i < coinsList.size(); i++) {

            if (balanceAmount < coinsList.get(i)) {
                coinsList.remove(coinsList.get(i));
            } else {
                if (balanceAmount == 0) {
                    break;
                }
                lists.add(getCoins(coinsList.get(i)));
                balanceAmount = subtract(balanceAmount, coinsList.get(i));
            }
            i--;
        }
        return lists;
    }

    private double subtract(double minuend, double subtrahend) {
        BigDecimal b1 = new BigDecimal(Double.toString(minuend));
        BigDecimal b2 = new BigDecimal(Double.toString(subtrahend));
        return b1.subtract(b2).doubleValue();
    }

    private Coin getCoins(Double coin) {
        Coin COIN = null;
        if (coin == 0.05d) {
            COIN = FIVE_CENTS;
        } else if (coin == 0.10d) {
            COIN = TEN_CENTS;
        } else if (coin == 0.20d) {
            COIN = TWENTY_CENTS;
        } else if (coin == 0.50d) {
            COIN = FIFTY_CENTS;
        } else if (coin == 1d) {
            COIN = ONE_DOLLAR;
        } else if (coin == 2d) {
            COIN = TWO_DOLLAR;
        }
        return COIN;


    }

    private double getCurrentTransactionAmount(Map<Coin, Integer> coinCounts) {

        return coinCounts.keySet().stream().mapToDouble(coin -> (coin.getValue() * coinCounts.get(coin))).sum();
    }

    public boolean validateQuantity(int availableQuantity, int requestedQuantity) {
        if (availableQuantity < requestedQuantity) {
            throw new RuntimeException("Quantity not available");
        }
        return true;
    }

    public boolean validateCoins(double sum, double totalAmount) {
        if (sum < totalAmount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        return true;
    }
}
