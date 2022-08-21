package com.example.gamestore.util.commands;

import com.example.gamestore.services.GameService;
import com.example.gamestore.util.messages.GameMessages;

@Cmd
public class DetailGameCommand implements Command {
    private GameService gameService;

    public DetailGameCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String execute(String... args) {
        try {
            return this.gameService.getGameDetails(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return GameMessages.MISSING_TITLE;
        }
    }
}
