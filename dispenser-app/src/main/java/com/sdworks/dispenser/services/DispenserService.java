package com.sdworks.dispenser.services;

import com.sdworks.dispenser.model.DrinksModel;
import com.sdworks.dispenser.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DispenserService {

    private static final Set<Double> COINS = Set.of(0.05d, 0.10d, 0.20d, 0.50d, 1d, 2d);

    @Autowired
    private ProductsService productsService;

    public boolean insertCoin(Double amount) {
        if (!COINS.contains(amount)) {
            throw new RuntimeException("Coins should in " + String.join(",", "" + COINS));
        }
        return true;
    }

    public DrinksModel dispenseDrink(boolean cancel, String code, Double coin, int noOfUnits) {
        insertCoin(coin);
        if (cancel) {
            return new DrinksModel(null, null, 0, coin);
        }
        return productsService.dispense(code, coin, noOfUnits);
    }

    public Set<ProductModel> getDrinksStatus() {
        return productsService.getDrinksStatus();
    }


}
