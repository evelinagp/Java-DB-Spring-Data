package com.example.gamestore.domain.models;

import java.util.Set;

public class OrderCartModel {
    private Integer id;
    private Set<GameCartModel> products;
    private UserBuyerModel buyer;

    public OrderCartModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<GameCartModel> getProducts() {
        return products;
    }

    public void setProducts(Set<GameCartModel> products) {
        this.products = products;
    }

    public UserBuyerModel getBuyer() {
        return buyer;
    }

    public void setBuyer(UserBuyerModel buyer) {
        this.buyer = buyer;
    }
}