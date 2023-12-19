package com.sdworx.dispenser.model;

import com.sdworx.dispenser.enums.COIN;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DrinkResponseModel {

    private String drinkCode;
    private String drinkName;
    private int noOfItems;
    private List<COIN> balanceAmount;

}
