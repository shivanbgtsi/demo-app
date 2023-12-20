package com.sdworx.dispenser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.model.DrinkModel;
import com.sdworx.dispenser.services.ProductsService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductsService productsService;

    @Test
    void create_returns_DrinkObject() throws Exception {
        DrinkModel drinkModel = new DrinkModel("TS", "Test", 1.0d, 100, 100);
        Drink drink = new Drink(drinkModel.getDrinkCode(), drinkModel.getDrinkName(), drinkModel.getProductPrice(),
                drinkModel.getMaxLimit(), drinkModel.getAvailableQuantity());
        when(productsService.saveProduct(ArgumentMatchers.any())).thenReturn(drink);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/product").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(drinkModel));
        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("drinkCode").value(drinkModel.getDrinkCode()))
                .andExpect(jsonPath("drinkName").value(drinkModel.getDrinkName()))
                .andExpect(jsonPath("availableQuantity").value(drinkModel.getAvailableQuantity()));
    }

    @Test
    void getProducts_returns_SetOfDrinkModelWithStatusCodeAsOk() throws Exception {
        DrinkModel drinkModel = new DrinkModel("TS", "Test", 1.0d, 100, 100);
        when(productsService.getProducts()).thenReturn(Set.of(drinkModel));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/product").
                contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns_StatusCodeAsNoContent() throws Exception {
        doNothing().when(productsService).deleteProducts(anyString());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/product/test").
                contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isNoContent());
    }

}