package com.example.gamestore.services;

import com.example.gamestore.domain.models.GameCartModel;
import com.example.gamestore.domain.models.OrderCartModel;
import com.example.gamestore.domain.models.UserBuyerModel;
import com.example.gamestore.services.session.UserSession;
import com.example.gamestore.util.messages.CartMessages;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {
    private UserService userService;
    private UserSession userSession;
    private GameService gameService;
    private OrderService orderService;

    public CartService(UserService userService, UserSession userSession, GameService gameService, OrderService orderService) {
        this.userService = userService;
        this.userSession = userSession;
        this.gameService = gameService;
        this.orderService = orderService;
    }

    public String addItem(String title) {
        if (!this.userSession.hasUserLogged()) {
            return CartMessages.NO_USER_LOGGED;
        }

        if (userSession.cartContainsGame(title)) {
            return String.format(CartMessages.CART_CONTAINS_GAME, title);
        }

        try {
            GameCartModel game = this.gameService.getGameCartModelByTitle(title);
            this.userSession.addToCart(game);

            return String.format(CartMessages.SUCC_ADDED_TO_CART, title);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String removeItem(String title) {
        if (!this.userSession.hasUserLogged()) {
            return CartMessages.NO_USER_LOGGED;
        }

        if (!this.userSession.removeFromCart(title)) {
            return String.format(CartMessages.CART_DOES_NOT_CONTAIN_GAME, title);
        }

        return String.format(CartMessages.SCC_REMOVED_FROM_CART, title);
    }

    public String buyItem() {
        if (!this.userSession.hasUserLogged()) {
            return CartMessages.NO_USER_LOGGED;
        }

        Set<GameCartModel> games = this.userSession.getCart();

        if (games.isEmpty()) {
            return CartMessages.EMPTY_CART;
        }

        UserBuyerModel buyer = this.userService.getBuyerById(this.userSession.getUser().getId());
        buyer.getGames().addAll(games);

        OrderCartModel order = new OrderCartModel();
        order.setProducts(games);
        order.setBuyer(buyer);
        buyer.getOrders().add(order);

        this.userService.buyGames(buyer);
        this.orderService.buyGames(order);
        this.userSession.clearCart();

        String gamesList = games.stream()
                .map(game -> String.format("-%s", game.getTitle()))
                .collect(Collectors.joining("\r\n"));

        return String.format(CartMessages.SUCC_BOUGHT_GAMES, gamesList);
    }
}
