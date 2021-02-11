package com.test.cafe.cafe.models;

import lombok.Getter;

@Getter
public class Ingredient {
    private String name;
    private Integer totalQuantity;
    private Integer consumed = 0;

    public Ingredient(String name, Integer quantity) {
        this.name = name;
        this.totalQuantity = quantity;
    }

}
