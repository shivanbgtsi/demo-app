package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Dispenser;
import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.enums.COINS;
import com.sdworx.dispenser.exception.InsufficientFundsException;
import com.sdworx.dispenser.exception.NotFoundException;
import com.sdworx.dispenser.model.DrinksModel;
import com.sdworx.dispenser.model.ProductModel;
import com.sdworx.dispenser.validators.DispenserDrinkValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class DispenseServiceTest {

    @InjectMocks
    private DispenserService dispenserService;

    @Mock
    private ProductsService productsService;

    @Mock
    private Dispenser dispenser;

    @Mock
    private DispenserDrinkValidations dispenserDrinkValidations;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void dispenseDrink_WhenInvalidDrinkCode_ThrowsNotFoundException() {
        when(productsService.getProductByDrinkCode(anyString()))
                .thenThrow(new NotFoundException("Drink code not found"));
        assertThrows(NotFoundException.class,
                () -> dispenserService.dispenseDrink("TEST", 1));
    }

    @Test
    void dispenseDrink_WhenQuantityNotAvailable_ThrowsRunTimeException() {
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);

        when(productsService.getProductByDrinkCode(anyString())).thenReturn(water);
        when(dispenserDrinkValidations.validateQuantity(anyInt(), anyInt())).thenThrow(new RuntimeException("Quantity not available"));
        assertThrows(RuntimeException.class,
                () -> dispenserService.dispenseDrink("WH", 1));
    }

    @Test
    void dispenseDrink_WhenInsufficientFunds_ThrowsInSufficientFundsException() {
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);

        when(productsService.getProductByDrinkCode(anyString())).thenReturn(water);
        when(dispenserDrinkValidations.validateCoins(anyDouble(), anyDouble())).thenThrow(new InsufficientFundsException("Insufficient funds"));
        assertThrows(InsufficientFundsException.class,
                () -> dispenserService.dispenseDrink("WH", 1));
    }

    @Test
    void dispenseDrink_WhenValidInputs_ReturnsDrinksDetails() {
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);

        when(productsService.getProductByDrinkCode(anyString())).thenReturn(water);
        when(dispenser.getSum()).thenReturn(1d);
        DrinksModel response = dispenserService.dispenseDrink("WH", 1);

        assertEquals(0.95, response.getBalanceAmount());
        assertEquals(water.getDrinkCode(), response.getDrinkCode());
        assertEquals(water.getDrinkName(), response.getDrinkName());
        assertEquals(1, response.getNoOfItems());
    }

    @Test
    void getDrinksStatus_ThrowsNotFoundException() {
        when(productsService.getProducts()).thenThrow(new NotFoundException("Product Not available"));
        assertThrows(NotFoundException.class, () -> dispenserService.getProducts());
    }

    @Test
    void getDrinkStatus_ReturnsDrinkModels() {
        ProductModel productModel = new ProductModel("WH", "Water", 5d, 10, 10);
        ProductModel productModelRB = new ProductModel("RB", "Red Bull", 5d, 10, 10);
        when(productsService.getProducts()).thenReturn(Set.of(productModelRB, productModel));
        Set<ProductModel> drinksStatus = dispenserService.getProducts();
        assertEquals(2, drinksStatus.size());
    }

    @Test
    void cancelOrder_ReturnsAllAmount() {
        when(dispenser.getCurrentTransactionCount()).thenReturn(List.of(COINS.FIFTY_CENTS));
        List<COINS> coins = dispenserService.cancelOrder();
        assertEquals(coins, List.of(COINS.FIFTY_CENTS));
    }

    @Test
    void insertCoin() {
        dispenserService.insertCoin(COINS.FIFTY_CENTS);

    }

}
