package com.sdworx.dispenser.controller;

import com.sdworx.dispenser.model.DrinkResponseModel;
import com.sdworx.dispenser.services.DrinkDispenserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drink")
@AllArgsConstructor
public class DispenseController {
    
    private final DrinkDispenserService drinkDispenserService;

    @GetMapping
    public DrinkResponseModel getDrink(@RequestParam("drinkCode") String drinkCode,
                                       @RequestParam("noOfItems") Integer noOfItems) {
        return drinkDispenserService.dispense(drinkCode, noOfItems);
    }

}
