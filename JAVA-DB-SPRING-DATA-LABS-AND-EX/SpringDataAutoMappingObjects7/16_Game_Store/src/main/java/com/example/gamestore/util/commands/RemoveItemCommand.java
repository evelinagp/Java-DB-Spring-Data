package com.example.gamestore.util.commands;

import com.example.gamestore.services.CartService;
import com.example.gamestore.util.messages.CartMessages;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

@Cmd
public class RemoveItemCommand implements Command{
    private CartService cartService;

    public RemoveItemCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public String execute(String... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException {
        try {
            return this.cartService.removeItem(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return CartMessages.MISSING_TITLE;
        }
    }
}