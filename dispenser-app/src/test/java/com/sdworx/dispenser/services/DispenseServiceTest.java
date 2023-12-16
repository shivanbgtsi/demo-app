package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Dispenser;
import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.enums.COINS;
import com.sdworx.dispenser.exception.InsufficientFundsException;
import com.sdworx.dispenser.exception.NotFoundException;
import com.sdworx.dispenser.model.DrinkModel;
import com.sdworx.dispenser.model.DrinkResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class DispenseServiceTest {

    @InjectMocks
    private DispenserService dispenserService;

    @Mock
    private ProductsService productsService;

    @Mock
    private Dispenser dispenser;

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
        assertThrows(RuntimeException.class,
                () -> dispenserService.dispenseDrink("WH", 1));
    }

    @Test
    void dispenseDrink_WhenInsufficientFunds_ThrowsInSufficientFundsException() {
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);

        when(productsService.getProductByDrinkCode(anyString())).thenReturn(water);
        assertThrows(InsufficientFundsException.class,
                () -> dispenserService.dispenseDrink("WH", 1));
    }

    @Test
    void dispenseDrink_WhenValidInputs_ReturnsDrinksDetails() {
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);
        Map<COINS, Integer> coinCounts = Map.of(COINS.FIFTY_CENTS, 1);
        when(dispenser.getCoinCounts()).thenReturn(coinCounts);
        when(productsService.getProductByDrinkCode(anyString())).thenReturn(water);
        DrinkResponseModel response = dispenserService.dispenseDrink("WH", 1);

        assertNotNull(response.getBalanceAmount());
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
        DrinkModel drinkModel = new DrinkModel("WH", "Water", 5d, 10, 10);
        DrinkModel drinkModelRB = new DrinkModel("RB", "Red Bull", 5d, 10, 10);
        when(productsService.getProducts()).thenReturn(Set.of(drinkModelRB, drinkModel));
        Set<DrinkModel> drinksStatus = dispenserService.getProducts();
        assertEquals(2, drinksStatus.size());
    }

    @Test
    void cancelOrder_ReturnsAllAmount() {
        Map<COINS, Integer> coinCount = Map.of(COINS.FIFTY_CENTS, 1);
        when(dispenser.getCoinCounts()).thenReturn(coinCount);
        List<COINS> coins = dispenserService.cancelOrder();
        assertEquals(coins, List.of(COINS.FIFTY_CENTS));
    }

    @Test
    void insertCoin_ReturnsMessage() {
        String message = dispenserService.insertCoin(COINS.FIFTY_CENTS);
        assertEquals("Choose 1. Insert coin 2. Drink 3. Cancel", message);

    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
}
