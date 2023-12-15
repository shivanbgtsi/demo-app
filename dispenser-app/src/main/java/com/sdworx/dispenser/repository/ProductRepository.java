package com.sdworx.dispenser.repository;

import com.sdworx.dispenser.entity.Drink;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepository {

    private static final Map<String, Drink> products = new HashMap<>();

    static {
        Drink coca = new Drink("CO", "Coca", 1d, 100, 100);
        Drink redBull = new Drink("RB", "Red Bull", 1.25d, 50, 100);
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);
        Drink orangeJuice = new Drink("OJ", "Orange Juice", 1.95d, 60, 100);
        products.put("CO", coca);
        products.put("RB", redBull);
        products.put("WH", water);
        products.put("OJ", orangeJuice);
    }

    public Optional<Drink> findByDrinkCode(String drinkCode) {
        return Optional.ofNullable(products.get(drinkCode));
    }

    public void updateProduct(Drink drink) {
        products.put(drink.getDrinkCode(), drink);
    }

    public Drink saveProduct(Drink drink) {
        products.put(drink.getDrinkCode(), drink);
        return drink;
    }

    public Optional<Map<String, Drink>> findAll() {
        return Optional.of(products);
    }

}
