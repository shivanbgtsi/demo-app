package com.sdworx.dispenser.entity;

import com.sdworx.dispenser.enums.COIN;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Dispenser {

    private double totalRevenue;
    private Map<COIN, Integer> coinCounts = new HashMap<>();

}
