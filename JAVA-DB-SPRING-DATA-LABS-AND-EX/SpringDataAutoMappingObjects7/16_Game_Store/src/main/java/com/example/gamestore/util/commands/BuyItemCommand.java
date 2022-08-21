package com.example.gamestore.util.commands;

import com.example.gamestore.services.CartService;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

    @Cmd
    public class BuyItemCommand implements Command {
        private CartService cartService;

        public BuyItemCommand(CartService cartService) {
            this.cartService = cartService;
        }

        @Override
        public String execute(String... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException {
            return this.cartService.buyItem();
        }
    }
