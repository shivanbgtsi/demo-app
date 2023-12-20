package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.exception.NotFoundException;
import com.sdworx.dispenser.model.DrinkModel;
import com.sdworx.dispenser.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductsServiceTest {

    @InjectMocks
    private ProductsService productsService;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductByDrinkCode_WhenInvalidDrinkCode_ThrowsNotFoundException() {
        when(productRepository.findByDrinkCode(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> productsService.getProductByDrinkCode("TEST"));
    }

    @Test
    void getProductByDrinkCode_WhenValidInputs_ReturnsDrinksDetails() {
        Drink product = new Drink("WH", "Water", 0.5d, 100, 100);
        when(productRepository.findByDrinkCode(anyString())).thenReturn(Optional.of(product));
        Drink response = productsService.getProductByDrinkCode("WH");

        assertEquals(product.getDrinkCode(), response.getDrinkCode());
        assertEquals(product.getDrinkName(), response.getDrinkName());
        assertEquals(product.getAvailableQuantity(), response.getAvailableQuantity());
    }

    @Test
    void saveProduct_WhenDrinkExists_ThrowsRTE() {
        DrinkModel drinkModel = new DrinkModel("WH", "Water", 5d, 10, 10);
        Drink mockedProduct = new
                Drink(drinkModel.getDrinkCode(), drinkModel.getDrinkName(), drinkModel.getProductPrice(), drinkModel.getMaxLimit(), drinkModel.getMaxLimit());
        when(productRepository.findByDrinkCode(any())).thenReturn(Optional.of(mockedProduct));
        assertThrows(RuntimeException.class, () -> {
            productsService.saveProduct(drinkModel);
        });
    }

    @Test
    void saveProduct_WhenAvailableQuantityIsMoreThanMaxQuantity_ThrowsRTE() {
        DrinkModel drinkModel = new DrinkModel("WH", "Water", 5d, 10, 25);

        when(productRepository.findByDrinkCode(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            productsService.saveProduct(drinkModel);
        });
    }

    @Test
    void saveProduct_ReturnsProduct() {
        DrinkModel drinkModel = new DrinkModel("WH", "Water", 5d, 10, 10);
        Drink mockedProduct = new
                Drink(drinkModel.getDrinkCode(), drinkModel.getDrinkName(), drinkModel.getProductPrice(), drinkModel.getMaxLimit(), drinkModel.getMaxLimit());
        when(productRepository.save(any())).thenReturn(mockedProduct);
        Drink product = productsService.saveProduct(drinkModel);

        assertEquals(product.getPrice(), drinkModel.getProductPrice());
        assertEquals(product.getAvailableQuantity(), drinkModel.getAvailableQuantity());
        assertEquals(product.getDrinkCode(), drinkModel.getDrinkCode());
        assertEquals(product.getDrinkName(), drinkModel.getDrinkName());
    }

    @Test
    void getDrinksStatus_ReturnsProductModel() {
        Drink product = new Drink("WH", "Water", 5d, 10, 10);
        Drink productRB = new Drink("RB", "Red Bull", 5d, 10, 10);
        Optional<Map<String, Drink>> mockedMap = Optional.of(Map.of("WH", product, "RB", productRB));
        when(productRepository.findAll()).thenReturn(mockedMap);
        Set<DrinkModel> drinksStatus = productsService.getProducts();
        assertEquals(2, drinksStatus.size());
    }


    @Test
    void update_WhenDrinkNotExists_ThrowsRTE() {
        DrinkModel drinkModel = new DrinkModel("WH", "Water", 5d, 10, 10);

        when(productRepository.findByDrinkCode(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            productsService.updateProduct(drinkModel);
        });
    }

    @Test
    void update_WhenAvailableQuantityIsMoreThanMaxQuantity_ThrowsRTE() {
        DrinkModel drinkModel = new DrinkModel("WH", "Water", 5d, 10, 25);

        Drink mockedProduct = new
                Drink(drinkModel.getDrinkCode(), drinkModel.getDrinkName(), drinkModel.getProductPrice(), drinkModel.getMaxLimit(), drinkModel.getMaxLimit());
        when(productRepository.findByDrinkCode(any())).thenReturn(Optional.of(mockedProduct));
        assertThrows(RuntimeException.class, () -> {
            productsService.saveProduct(drinkModel);
        });
    }

    @Test
    void update_ReturnsProduct() {
        DrinkModel drinkModel = new DrinkModel("WH", "Water", 5d, 10, 10);
        Drink mockedProduct = new
                Drink(drinkModel.getDrinkCode(), drinkModel.getDrinkName(), drinkModel.getProductPrice(), drinkModel.getMaxLimit(), drinkModel.getMaxLimit());
        when(productRepository.findByDrinkCode(any())).thenReturn(Optional.of(mockedProduct));
        doNothing().when(productRepository).update(any());

        productsService.updateProduct(drinkModel);
        verify(productRepository, times(1)).update(any());
    }

}
