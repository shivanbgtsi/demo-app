package com.sdworx.dispenser.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DrinksModel {

    private String drinkCode;
    private String drinkName;
    private int noOfItems;
    private Double balanceAmount;

}
