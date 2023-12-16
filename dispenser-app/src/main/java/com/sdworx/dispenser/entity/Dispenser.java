package com.sdworx.dispenser.entity;

import com.sdworx.dispenser.enums.COINS;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Dispenser {

    private double totalRevenue;
    private Map<COINS, Integer> coinCounts = new HashMap<>();


}
