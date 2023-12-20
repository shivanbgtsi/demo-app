package com.sdworx.dispenser.controller;

import com.sdworx.dispenser.enums.Coin;
import com.sdworx.dispenser.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<String> insertCoin(@RequestParam("coin") Coin coin) {
        return new ResponseEntity<String>(orderService.insertCoin(coin), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Coin>> cancelOrder() {
        return new ResponseEntity<List<Coin>>(orderService.cancelOrder(), HttpStatus.OK);
    }

}
