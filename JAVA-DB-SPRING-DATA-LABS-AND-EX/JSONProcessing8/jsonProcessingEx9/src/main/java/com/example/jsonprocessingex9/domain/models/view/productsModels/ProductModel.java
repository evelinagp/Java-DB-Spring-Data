package com.example.jsonprocessingex9.domain.models.view.productsModels;

import java.math.BigDecimal;

public class ProductModel {
    private String name;
    private BigDecimal price;

    public ProductModel() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
