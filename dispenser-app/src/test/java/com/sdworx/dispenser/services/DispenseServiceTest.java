package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Dispenser;
import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.enums.Coin;
import com.sdworx.dispenser.exception.InsufficientFundsException;
import com.sdworx.dispenser.exception.NotFoundException;
import com.sdworx.dispenser.model.DrinkModel;
import com.sdworx.dispenser.model.DrinkResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Set;

import static com.sdworx.dispenser.enums.Coin.FIFTY_CENTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class DispenseServiceTest {

    @InjectMocks
    private DrinkDispenserService drinkDispenserService;

    @Mock
    private ProductsService productsService;

    @Mock
    private Dispenser dispenser;

    @Test
    void dispenseDrink_WhenInvalidDrinkCode_ThrowsNotFoundException() {
        when(productsService.getProductByDrinkCode(anyString()))
                .thenThrow(new NotFoundException("Drink code not found"));
        assertThrows(NotFoundException.class,
                () -> drinkDispenserService.dispense("TEST", 1));
    }

    @Test
    void dispenseDrink_WhenQuantityNotAvailable_ThrowsRunTimeException() {
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);

        when(productsService.getProductByDrinkCode(anyString())).thenReturn(water);
        assertThrows(RuntimeException.class,
                () -> drinkDispenserService.dispense("WH", 1));
    }

    @Test
    void dispenseDrink_WhenInsufficientFunds_ThrowsInSufficientFundsException() {
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);

        when(productsService.getProductByDrinkCode(anyString())).thenReturn(water);
        assertThrows(InsufficientFundsException.class,
                () -> drinkDispenserService.dispense("WH", 1));
    }

    @Test
    void dispenseDrink_WhenValidInputs_ReturnsDrinksDetails() {
        Drink water = new Drink("WH", "Water", 0.05d, 100, 100);
        Map<Coin, Integer> coinCounts = Map.of(FIFTY_CENTS, 1);
        when(dispenser.getCoinCounts()).thenReturn(coinCounts);
        when(productsService.getProductByDrinkCode(anyString())).thenReturn(water);
        DrinkResponseModel response = drinkDispenserService.dispense("WH", 1);

        assertNotNull(response.getBalanceAmount());
        assertEquals(water.getDrinkCode(), response.getDrinkCode());
        assertEquals(water.getDrinkName(), response.getDrinkName());
        assertEquals(1, response.getNoOfItems());
    }

    @Test
    void getDrinksStatus_ThrowsNotFoundException() {
        when(productsService.getProducts()).thenThrow(new NotFoundException("Product Not available"));
        assertThrows(NotFoundException.class, () -> drinkDispenserService.getProducts());
    }

    @Test
    void getDrinkStatus_ReturnsDrinkModels() {
        DrinkModel drinkModel = new DrinkModel("WH", "Water", 5d, 10, 10);
        DrinkModel drinkModelRB = new DrinkModel("RB", "Red Bull", 5d, 10, 10);
        when(productsService.getProducts()).thenReturn(Set.of(drinkModelRB, drinkModel));
        Set<DrinkModel> drinksStatus = drinkDispenserService.getProducts();
        assertEquals(2, drinksStatus.size());
    }

//    @Test
//    void test() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Drink> stringDrinkMap = mapper.readValue(new File("src/main/resources/drink-resource.json"), new TypeReference<Map<String, Drink>>() {
//        });
//        System.out.println(stringDrinkMap);
////        Drink drink = stringDrinkMap.get("WH");
////        drink.setAvailableQuantity(60);
////        stringDrinkMap.put("WH", drink);
//
//        Drink drink = new Drink("TE", "TEST", 0.5d, 100, 100);
//        stringDrinkMap.put(drink.getDrinkCode(), drink);
//        mapper.writeValue(new File("src/main/resources/drink-resource.json"), stringDrinkMap);
////        mapper.writeValueAsString(new File("config.json"),stringDrinkMap);
//    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
}
