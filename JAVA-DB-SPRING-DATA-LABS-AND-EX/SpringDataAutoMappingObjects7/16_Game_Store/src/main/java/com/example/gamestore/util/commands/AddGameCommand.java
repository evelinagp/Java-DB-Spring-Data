package com.example.gamestore.util.commands;

import com.example.gamestore.services.GameService;

import java.text.ParseException;

@Cmd
public class AddGameCommand implements Command {
    private GameService gameService;

    private static int NUMBER_OF_NEEDED_ARGS = 7;

    public AddGameCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String execute(String... args) throws ParseException {
        if (args.length != NUMBER_OF_NEEDED_ARGS) {
            return "Fill all fields to add game.";
        }

        return this.gameService.addGame(args);
    }
}

