package com.example.gamestore.util.commands;

import com.example.gamestore.services.CartService;
import com.example.gamestore.util.messages.CartMessages;

    @Cmd
    public class AddItemCommand implements Command{
        private CartService cartService;

        public AddItemCommand(CartService cartService) {
            this.cartService = cartService;
        }

        @Override
        public String execute(String... args) {
            try {
                return this.cartService.addItem(args[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                return CartMessages.MISSING_TITLE;
            }
        }
    }
