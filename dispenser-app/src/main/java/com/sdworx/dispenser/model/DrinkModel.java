package com.sdworx.dispenser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DrinkModel {

    private String drinkCode;
    private String drinkName;
    private Double productPrice;
    private int maxLimit;
    private int availableQuantity;
    
}
