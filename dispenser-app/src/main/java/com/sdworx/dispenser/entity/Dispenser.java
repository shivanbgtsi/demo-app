package com.sdworx.dispenser.entity;

import com.sdworx.dispenser.enums.Coin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Component
public class Dispenser {

    private double totalRevenue;
    private Map<Coin, Integer> coinCounts = new HashMap<>();

}
