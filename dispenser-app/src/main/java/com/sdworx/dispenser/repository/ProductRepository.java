package com.sdworx.dispenser.repository;

import com.sdworx.dispenser.entity.Drink;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProductRepository {

    private static final Map<String, Drink> products = new HashMap<>();

    private DrinkStore drinkStore;

    @PostConstruct
    public void init() {
        products.putAll(drinkStore.getDrinkElementsFromFile());
    }

    public Optional<Drink> findByDrinkCode(String drinkCode) {
        return Optional.ofNullable(products.get(drinkCode));
    }

    public void update(Drink drink) {
        try {
            products.putAll(drinkStore.updateDrinkElementsToFile(drink));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Drink save(Drink drink) {
        try {
            drink = drinkStore.saveDrinkElementsToFile(drink);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return drink;
    }

    public Optional<Map<String, Drink>> findAll() {
        return Optional.of(products);
    }

    public void delete(String drinkCode) {
        try {
            drinkStore.deleteDrinkElementsFromFile(drinkCode);
            products.remove(drinkCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
