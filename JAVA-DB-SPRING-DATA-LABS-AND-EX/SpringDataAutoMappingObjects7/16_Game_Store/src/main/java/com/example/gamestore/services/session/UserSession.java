package com.example.gamestore.services.session;


import com.example.gamestore.domain.models.GameCartModel;
import com.example.gamestore.domain.models.UserModel;

import java.util.Set;

public interface UserSession {
    boolean hasUserLogged();

    boolean isAdmin();

    UserModel getUser();

    void logout();

    void login(UserModel user);

    void addToCart(GameCartModel game);

    boolean removeFromCart(String title);

    Set<GameCartModel> getCart();

    boolean cartContainsGame(String title);

    public void clearCart();
}
