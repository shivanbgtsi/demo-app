package com.sdworks.dispenser.services;

import com.sdworks.dispenser.exception.InsufficientFunds;
import com.sdworks.dispenser.exception.NotFoundException;
import com.sdworks.dispenser.model.DrinksModel;
import com.sdworks.dispenser.model.ProductModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DispenseServiceTest {

    @InjectMocks
    private DispenserService dispenserService;

    @Mock
    private ProductsService productsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertCoin_CoinIsOutOfRange_ThrowsRunTimeException() {
        assertThrows(RuntimeException.class,
                () -> dispenserService.insertCoin(50d));
    }

    @Test
    void insertCoin_CoinIsInAcceptedRange_ReturnTrue() {
        assertTrue(dispenserService.insertCoin(1d));
    }

    @Test
    void dispenseDrink_WhenInvalidDrinkCode_ThrowsNotFoundException() {
        when(productsService.dispense(anyString(), anyDouble(), anyInt()))
                .thenThrow(new NotFoundException("Drink code not found"));
        assertThrows(NotFoundException.class,
                () -> dispenserService.dispenseDrink(false, "TEST", 1d, 1));
    }

    @Test
    void dispenseDrink_WhenQuantityNotAvailable_ThrowsRunTimeException() {
        when(productsService.dispense(anyString(), anyDouble(), anyInt()))
                .thenThrow(new RuntimeException("Quantity not available"));
        assertThrows(RuntimeException.class,
                () -> dispenserService.dispenseDrink(false, "WH", 1d, 1));
    }

    @Test
    void dispenseDrink_WhenInsufficientFunds_ThrowsInSufficientFundsException() {
        when(productsService.dispense(anyString(), anyDouble(), anyInt()))
                .thenThrow(new InsufficientFunds("Insufficient funds"));
        assertThrows(InsufficientFunds.class,
                () -> dispenserService.dispenseDrink(false, "WH", 1d, 1));
    }

    @Test
    void dispenseDrink_WhenValidInputs_ReturnsDrinksDetails() {
        DrinksModel drinksModel = new DrinksModel("WH", "Water", 1, .50d);
        when(productsService.dispense(anyString(), anyDouble(), anyInt())).thenReturn(drinksModel);

        DrinksModel response = dispenserService.dispenseDrink(false, "WH", 1d, 1);
        
        assertEquals(drinksModel.getRemainingAmount(), response.getRemainingAmount());
        assertEquals(drinksModel.getDrinkCode(), response.getDrinkCode());
        assertEquals(drinksModel.getDrinkName(), response.getDrinkName());
        assertEquals(drinksModel.getNoOfItems(), response.getNoOfItems());
    }

    @Test
    void getDrinksStatus_ThrowsNotFoundException() {
        when(productsService.getDrinksStatus()).thenThrow(new NotFoundException("Product Not available"));
        assertThrows(NotFoundException.class, () -> dispenserService.getDrinksStatus());
    }

    @Test
    void getDrinkStatus_ReturnsDrinkModels() {
        ProductModel productModel = new ProductModel("WH", "Water", 5d, 10, true, 10);
        ProductModel productModelRB = new ProductModel("RB", "Red Bull", 5d, 10, false, 10);
        when(productsService.getDrinksStatus()).thenReturn(Set.of(productModelRB, productModel));
        Set<ProductModel> drinksStatus = dispenserService.getDrinksStatus();
        assertEquals(2, drinksStatus.size());

    }
}
