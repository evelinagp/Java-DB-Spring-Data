package com.example.gamestore.util.commands;

import com.example.gamestore.services.GameService;

import java.lang.reflect.InvocationTargetException;

@Cmd
public class EditGameCommand implements Command {
    private GameService gameService;

    public EditGameCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String execute(String... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return this.gameService.editGame(args);
    }
}
