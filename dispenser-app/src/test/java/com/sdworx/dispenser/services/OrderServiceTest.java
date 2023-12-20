package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Dispenser;
import com.sdworx.dispenser.enums.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static com.sdworx.dispenser.enums.Coin.FIFTY_CENTS;
import static com.sdworx.dispenser.enums.Coin.TEN_CENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private Dispenser dispenser;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cancelOrder_ReturnsAllAmount() {
        Map<Coin, Integer> coinCount = Map.of(FIFTY_CENTS, 1, TEN_CENTS, 5);
        when(dispenser.getCoinCounts()).thenReturn(coinCount);
        List<Coin> coins = orderService.cancelOrder();
        assertEquals(6, coins.size());
    }

    @Test
    void insertCoin_ReturnsMessage() {
        String message = orderService.insertCoin(FIFTY_CENTS);
        assertEquals("Choose 1. Insert coin 2. Drink 3. Cancel", message);

    }


}