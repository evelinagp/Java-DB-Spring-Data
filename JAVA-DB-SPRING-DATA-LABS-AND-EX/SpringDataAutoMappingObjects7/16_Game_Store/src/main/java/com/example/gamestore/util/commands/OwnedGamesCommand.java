package com.example.gamestore.util.commands;

import com.example.gamestore.services.GameService;

@Cmd
public class OwnedGamesCommand implements Command {
    private GameService gameService;

    public OwnedGamesCommand(GameService userService) {
        this.gameService = userService;
    }

    @Override
    public String execute(String... args) {
        return this.gameService.getOwnedGamesTitles();
    }
}