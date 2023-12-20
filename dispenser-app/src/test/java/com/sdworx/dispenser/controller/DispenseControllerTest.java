package com.sdworx.dispenser.controller;

import com.sdworx.dispenser.model.DrinkResponseModel;
import com.sdworx.dispenser.services.DrinkDispenserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DispenseController.class)
class DispenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrinkDispenserService drinkDispenserService;

    @Test
    void getProducts_returns_SetOfDrinkModelWithStatusCodeAsOk() throws Exception {
        DrinkResponseModel drinkModel = new DrinkResponseModel("TS", "Test", 100, List.of());
        when(drinkDispenserService.dispense(anyString(), anyInt())).thenReturn(drinkModel);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/drink?drinkCode=WH&noOfItems=2").
                contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("drinkCode").value(drinkModel.getDrinkCode()))
                .andExpect(jsonPath("drinkName").value(drinkModel.getDrinkName()))
                .andExpect(jsonPath("noOfItems").value(drinkModel.getNoOfItems()));
    }

}