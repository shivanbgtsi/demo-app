package com.sdworx.dispenser.controller;

import com.sdworx.dispenser.services.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void insertCoin_returns_String() throws Exception {
        when(orderService.insertCoin(ArgumentMatchers.any())).thenReturn("Choose 1. Insert coin 2. Drink 3. Cancel");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/order?coin=FIVE_CENTS").
                contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void cancelOrder_returns_ListOfCoins() throws Exception {
        when(orderService.insertCoin(ArgumentMatchers.any())).thenReturn("Choose 1. Insert coin 2. Drink 3. Cancel");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/order").
                contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk());
    }

}