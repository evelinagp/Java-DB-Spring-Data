package com.example.gamestore.util.commands;

import com.example.gamestore.services.GameService;

    @Cmd
    public class DeleteGameCommand implements Command {
        private GameService gameService;

        public DeleteGameCommand(GameService gameService) {
            this.gameService = gameService;
        }

        @Override
        public String execute(String... args) {
            try {
                return this.gameService.deleteGame(args[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                return "Missing game id.";
            }
        }
    }
