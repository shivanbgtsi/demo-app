package com.sdworks.dispenser.services;

import com.sdworks.dispenser.entity.Product;
import com.sdworks.dispenser.exception.InsufficientFunds;
import com.sdworks.dispenser.exception.NotFoundException;
import com.sdworks.dispenser.model.DrinksModel;
import com.sdworks.dispenser.model.ProductModel;
import com.sdworks.dispenser.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
    void dispense_WhenInvalidDrinkCode_ThrowsNotFoundException() {
        when(productRepository.findByDrinkCode(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> productsService.dispense("TEST", 1d, 1));
    }

    @Test
    void dispense_WhenQuantityNotAvailable_ThrowsRunTimeException() {
        Product product = new Product("WH", "Water", 1d, 100, true);
        product.setAvailableLimit(0);
        when(productRepository.findByDrinkCode(anyString())).thenReturn(Optional.of(product));
        assertThrows(RuntimeException.class, () -> productsService.dispense("WH", 1d, 1));
    }

    @Test
    void dispense_WhenInsufficientFunds_ThrowsInSufficientFundsException() {
        Product product = new Product("WH", "Water", 1d, 100, true);
        when(productRepository.findByDrinkCode(anyString())).thenReturn(Optional.of(product));
        assertThrows(InsufficientFunds.class, () -> productsService.dispense("WH", 1d, 2));
    }

    @Test
    void dispense_WhenValidInputs_ReturnsDrinksDetails() {
        Product product = new Product("WH", "Water", 0.5d, 100, true);
        when(productRepository.findByDrinkCode(anyString())).thenReturn(Optional.of(product));
        DrinksModel response = productsService.dispense("WH", 1d, 1);

        assertEquals(product.getDrinkCode(), response.getDrinkCode());
        assertEquals(product.getDrinkName(), response.getDrinkName());
        assertEquals(1, response.getNoOfItems());
        assertEquals(0.5d, response.getRemainingAmount());
    }

    @Test
    void saveProduct_ReturnsProduct() {
        ProductModel productModel = new ProductModel("WH", "Water", 5d, 10, true, 10);
        Product product = new
                Product(productModel.getDrinkCode(), productModel.getDrinkName(), productModel.getProductPrice(), productModel.getMaxLimit(), productModel.isAvailable());
        when(productRepository.saveProduct(product)).thenReturn(product);
        Product product1 = productsService.saveProduct(productModel);

        assertEquals(product1.getProductPrice(), productModel.getProductPrice());
        assertEquals(product1.getAvailableLimit(), productModel.getAvailableLimit());
        assertEquals(product1.getDrinkCode(), productModel.getDrinkCode());
        assertEquals(product1.getDrinkName(), productModel.getDrinkName());
    }

    @Test
    void getDrinksStatus_ReturnsProductModel() {

        Product product = new Product("WH", "Water", 5d, 10, true);
        Product productRB = new Product("RB", "Red Bull", 5d, 10, false);
        when(productRepository.findAll()).thenReturn(Optional.of(Set.of(productRB, product)));
        Set<ProductModel> drinksStatus = productsService.getDrinksStatus();
        assertEquals(2, drinksStatus.size());

    }

}
