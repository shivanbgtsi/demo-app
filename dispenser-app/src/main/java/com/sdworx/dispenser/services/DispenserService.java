package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Dispenser;
import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.enums.COINS;
import com.sdworx.dispenser.exception.InsufficientFundsException;
import com.sdworx.dispenser.model.DrinkModel;
import com.sdworx.dispenser.model.DrinkResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DispenserService {

    @Autowired
    private final ProductsService productsService;
    private Dispenser dispenser;

    public DispenserService(ProductsService productsService) {
        this.productsService = productsService;
    }


    @PostConstruct
    public void initializeDispenser() {
        dispenser = new Dispenser();
    }

    public String insertCoin(COINS coin) {
        Map<COINS, Integer> coinCounts = dispenser.getCoinCounts();

        if (coinCounts.containsKey(coin)) {
            coinCounts.put(coin, coinCounts.get(coin) + 1);
        } else {
            coinCounts.put(coin, 1);
        }
        dispenser.setCoinCounts(coinCounts);
        return "Choose 1. Insert coin 2. Drink 3. Cancel";
    }

    public List<COINS> cancelOrder() {
        Map<COINS, Integer> coinCounts = dispenser.getCoinCounts();
        List<COINS> coinsList = new ArrayList<>();
//        coinCounts.keySet().stream().map(x ->
//        {
//            for (int j = 0; j < coinCounts.get(x); j++) {
//                coinsList.add(x);
//            }
//            return coinsList;
//        });
        Set<COINS> coins1 = coinCounts.keySet();
        for (COINS coin : coins1) {
            for (int i = 0; i < coinCounts.get(coin); i++) {
                coinsList.add(coin);
            }
        }
        dispenser.setCoinCounts(new HashMap<>());
        return coinsList;
    }

    public DrinkResponseModel dispenseDrink(String drinkCode, int noOfUnits) {
        Map<COINS, Integer> coinCounts = dispenser.getCoinCounts();
        double sum = getCurrentTransactionAmount(coinCounts);
        Drink drink = productsService.getProductByDrinkCode(drinkCode);

        validateQuantity(drink.getAvailableQuantity(), noOfUnits);
        double totalAmount = drink.getPrice() * noOfUnits;
        validateCoins(sum, totalAmount);
        drink.setAvailableQuantity(drink.getMaxLimit() - noOfUnits);

        productsService.updateProduct(drink);

        dispenser.setTotalRevenue(dispenser.getTotalRevenue() + totalAmount);
        dispenser.setCoinCounts(new HashMap<>());

        return new DrinkResponseModel(drinkCode, drink.getDrinkName(), noOfUnits, getBalanceCoins(sum - totalAmount));
    }

    public Set<DrinkModel> getProducts() {
        return productsService.getProducts();
    }

    private List<COINS> getBalanceCoins(Double balanceAmount) {
        if (balanceAmount == 0) {
            return List.of();
        }
        List<Double> coinsList = Arrays.stream(COINS.values()).map(COINS::getValue)
                .sorted((o1, o2) -> Double.compare(o2, o1)).collect(Collectors.toList());

        if (coinsList.contains(balanceAmount)) {
            return List.of(getCoins(balanceAmount));
        }
        List<COINS> lists = new ArrayList<>();
        int i = 0;
        for (; i < coinsList.size(); i++) {

            if (balanceAmount < coinsList.get(i)) {
                coinsList.remove(coinsList.get(i));
            } else {
                if (balanceAmount == 0) {
                    break;
                }
                lists.add(getCoins(coinsList.get(i)));
                balanceAmount = sub(balanceAmount, coinsList.get(i));
            }
            i--;
        }
        return lists;
    }

    private double sub(double minuend, double subtrahend) {
        BigDecimal b1 = new BigDecimal(Double.toString(minuend));
        BigDecimal b2 = new BigDecimal(Double.toString(subtrahend));
        return b1.subtract(b2).doubleValue();

    }

    private COINS getCoins(Double coin) {
        COINS coins = null;
        if (coin == 0.05d) {
            coins = COINS.FIVE_CENTS;
        } else if (coin == 0.10d) {
            coins = COINS.TEN_CENTS;
        } else if (coin == 0.20d) {
            coins = COINS.TWENTY_CENTS;
        } else if (coin == 0.50d) {
            coins = COINS.FIFTY_CENTS;
        } else if (coin == 1d) {
            coins = COINS.ONE_DOLLAR;
        } else if (coin == 2d) {
            coins = COINS.TWO_DOLLAR;
        }
        return coins;


    }

    private double getCurrentTransactionAmount(Map<COINS, Integer> coinCounts) {

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
