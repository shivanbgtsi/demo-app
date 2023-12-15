package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.exception.NotFoundException;
import com.sdworx.dispenser.model.ProductModel;
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
                () -> productsService.getProductByDrinkCode("TEST"));
    }

    @Test
    void dispense_WhenValidInputs_ReturnsDrinksDetails() {
        Drink product = new Drink("WH", "Water", 0.5d, 100, 100);
        when(productRepository.findByDrinkCode(anyString())).thenReturn(Optional.of(product));
        Drink response = productsService.getProductByDrinkCode("WH");

        assertEquals(product.getDrinkCode(), response.getDrinkCode());
        assertEquals(product.getDrinkName(), response.getDrinkName());
        assertEquals(product.getAvailableQuantity(), response.getAvailableQuantity());
    }

    @Test
    void saveProduct_ReturnsProduct() {
        ProductModel productModel = new ProductModel("WH", "Water", 5d, 10, 10);
        Drink mockedProduct = new
                Drink(productModel.getDrinkCode(), productModel.getDrinkName(), productModel.getProductPrice(), productModel.getMaxLimit(), productModel.getMaxLimit());
        when(productRepository.saveProduct(any())).thenReturn(mockedProduct);
        Drink product = productsService.saveProduct(productModel);

        assertEquals(product.getPrice(), productModel.getProductPrice());
        assertEquals(product.getAvailableQuantity(), productModel.getAvailableQuantity());
        assertEquals(product.getDrinkCode(), productModel.getDrinkCode());
        assertEquals(product.getDrinkName(), productModel.getDrinkName());
    }

    @Test
    void getDrinksStatus_ReturnsProductModel() {
        Drink product = new Drink("WH", "Water", 5d, 10, 10);
        Drink productRB = new Drink("RB", "Red Bull", 5d, 10, 10);
        Optional<Map<String, Drink>> mockedMap = Optional.of(Map.of("WH", product, "RB", productRB));
        when(productRepository.findAll()).thenReturn(mockedMap);
        Set<ProductModel> drinksStatus = productsService.getProducts();
        assertEquals(2, drinksStatus.size());
    }

}
