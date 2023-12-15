package com.sdworx.dispenser.validators;

import com.sdworx.dispenser.exception.InsufficientFundsException;
import org.springframework.stereotype.Component;

@Component
public class DispenserDrinkValidations {

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
