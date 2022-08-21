package com.example.gamestore.domain.models;

import java.util.Set;

public class UserBuyerModel {
    private Integer id;
    private Set<GameCartModel> games;
    private Set<OrderCartModel> orders;

    public UserBuyerModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<GameCartModel> getGames() {
        return games;
    }

    public void setGames(Set<GameCartModel> games) {
        this.games = games;
    }

    public Set<OrderCartModel> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderCartModel> orders) {
        this.orders = orders;
    }
}
