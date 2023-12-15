package com.sdworx.dispenser.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Drink {

    private String drinkCode;
    private String drinkName;
    private Double price;
    private int maxLimit;
    private int availableQuantity;
}
