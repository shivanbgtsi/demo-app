package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Dispenser;
import com.sdworx.dispenser.enums.Coin;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@AllArgsConstructor
public class OrderService {

    private Dispenser dispenser;

    public String insertCoin(Coin coin) {
        Map<Coin, Integer> coinCounts = dispenser.getCoinCounts();

        if (coinCounts.containsKey(coin)) {
            coinCounts.put(coin, coinCounts.get(coin) + 1);
        }
        coinCounts.putIfAbsent(coin, 1);
        dispenser.setCoinCounts(coinCounts);
        return "Choose 1. Insert coin 2. Drink 3. Cancel";
    }

    public List<Coin> cancelOrder() {
        Map<Coin, Integer> coinCounts = dispenser.getCoinCounts();
        List<Coin> refundCoins = coinCounts.entrySet().stream().flatMap(entry ->
                Stream.iterate(entry.getKey(), s -> s)
                        .limit(entry.getValue())
        ).collect(Collectors.toList());

        dispenser.setCoinCounts(new HashMap<>());
        return refundCoins;
    }
    
}
