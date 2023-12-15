package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Dispenser;
import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.enums.COINS;
import com.sdworx.dispenser.model.DrinksModel;
import com.sdworx.dispenser.model.ProductModel;
import com.sdworx.dispenser.validators.DispenserDrinkValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Service
public class DispenserService {

    private Dispenser dispenser;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private DispenserDrinkValidations dispenserDrinkValidations;

    @PostConstruct
    public void initializeDispenser() {
        dispenser = new Dispenser();
    }

    public String insertCoin(COINS coin) {
        System.out.println("Collecting coins");
        dispenser.insertCoin(coin);
        return "Choose 1. Insert coin 2. Drink 3. Cancel";
    }

    public List<COINS> cancelOrder() {
        System.out.println("Cancelling order");
        List<COINS> currentTransactionCount = dispenser.getCurrentTransactionCount();
        dispenser.setCurrentTransaction();
        return currentTransactionCount;
    }

    public DrinksModel dispenseDrink(String drinkCode, int noOfUnits) {
        double sum = dispenser.getSum();
        Drink drink = productsService.getProductByDrinkCode(drinkCode);

        dispenserDrinkValidations.validateQuantity(drink.getAvailableQuantity(), noOfUnits);
        double totalAmount = drink.getPrice() * noOfUnits;
        dispenserDrinkValidations.validateCoins(sum, totalAmount);
        drink.setAvailableQuantity(drink.getMaxLimit() - noOfUnits);
        System.out.println("Updating product with available quantity");
        productsService.updateProduct(drink);
        dispenser.setTotalRevenue(totalAmount);
        System.out.println("Updating current transaction to zero");
        dispenser.setCurrentTransaction();
        return new DrinksModel(drinkCode, drink.getDrinkName(), noOfUnits, sum - totalAmount);
    }

    public Set<ProductModel> getProducts() {
        return productsService.getProducts();
    }

}
