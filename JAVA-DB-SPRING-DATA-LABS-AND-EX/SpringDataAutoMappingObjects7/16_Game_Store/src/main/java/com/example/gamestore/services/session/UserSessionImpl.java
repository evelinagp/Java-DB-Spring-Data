package com.example.gamestore.services.session;

import com.example.gamestore.domain.models.GameCartModel;
import com.example.gamestore.domain.models.UserModel;

import java.util.Set;

import com.example.gamestore.domain.entities.enums.UserType;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserSessionImpl implements UserSession {
    private UserModel currentUser;
    private Map<String, GameCartModel> cart;

    public UserSessionImpl(Map<String, GameCartModel> cart) {
        this.cart = cart;
    }

    @Override
    public boolean hasUserLogged() {
        return this.currentUser != null;
    }

    @Override
    public boolean isAdmin() {
        return this.getUser().getUserType().name().equals(UserType.ADMINISTRATOR.name());
    }

    @Override
    public UserModel getUser() {
        return this.currentUser;
    }

    @Override
    public void logout() {
        this.currentUser = null;
    }

    @Override
    public void login(UserModel user) {
        this.currentUser = user;
    }

    @Override
    public void addToCart(GameCartModel game) {
        this.cart.put(game.getTitle(), game);
    }

    @Override
    public boolean removeFromCart(String title) {
        return this.cart.remove(title) != null;
    }

    @Override
    public Set<GameCartModel> getCart() {
        return new HashSet<>(this.cart.values());
    }

    @Override
    public boolean cartContainsGame(String title) {
        return this.cart.containsKey(title);
    }

    @Override
    public void clearCart() {
        this.cart.clear();
    }
}