package com.test.cafe.cafe.models;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Beverage {
    private String name;
    private Map<String, Integer> ingredients;

    public Beverage(String name) {
        this.name = name;
    }
}
