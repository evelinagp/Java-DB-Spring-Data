package com.example.gamestore.util.io;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import com.example.gamestore.util.commands.Command;
import com.example.gamestore.services.AuthenticationService;
import com.example.gamestore.services.CartService;
import com.example.gamestore.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

    @Component
    public class InputHandlerImpl implements InputHandler {
        private final String COMMANDS_PACKAGE_NAME = "com.example.gamestore.util.commands";
        private Map<String, Command> commands;
        private final AuthenticationService authenticationService;
        private final GameService gameService;
        private final CartService cartService;

        @Autowired
        public InputHandlerImpl(AuthenticationService authenticationService, GameService gameService, CartService cartService) throws IOException, ClassNotFoundException {
            this.authenticationService = authenticationService;
            this.gameService = gameService;
            this.cartService = cartService;
            this.initHandler();
        }

        //Extract command classes then their names
        //Adds to commands map corresponding Command class instance with command name
        //No idea how to create instances of Command classes with automatic Dependency injection from Spring.

        @PostConstruct
        private void initHandler() throws IOException, ClassNotFoundException {
            this.commands = new HashMap<>();
            List<Class> commandClasses = PackageClassFinder.getCommandClasses(COMMANDS_PACKAGE_NAME);


            //If it fails to get constructor with authService as param it will create a command with gameService.
            //Trash and hard to extend the app without chaining endless try/catch blocks.
            commandClasses.forEach(commandClazz -> {
                try {
                    String commandName = commandClazz.getSimpleName().replace("Command", "");
                    Command command;
                    try {
                        command = (Command) commandClazz.getConstructor(this.authenticationService.getClass())
                                .newInstance(this.authenticationService);
                    } catch (NoSuchMethodException e) {
                        try {

                            command = (Command) commandClazz.getConstructor(this.gameService.getClass())
                                    .newInstance(this.gameService);
                        }catch (NoSuchMethodException ex) {
                            command = (Command) commandClazz.getConstructor(this.cartService.getClass()).newInstance(this.cartService);
                        }
                    }

                    commands.put(commandName, command);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
        }

        private String getCommandName(String input) {
            return input.split("\\|")[0].replace("User", "");
        }

        private String[] getCommandArgs(String input) {
            String[] commandLine = input.split("\\|");
            return Arrays.copyOfRange(commandLine, 1, commandLine.length);
        }

        @Override
        public String executeInput(String input) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException {
            String commandName = this.getCommandName(input);

            if (!isValidCommand(commandName)) {
                return "Invalid command!";
            }

            String[] commandArgs = this.getCommandArgs(input);

            return this.commands.get(commandName).execute(commandArgs);
        }

        private boolean isValidCommand(String commandName) {
            return this.commands.containsKey(commandName);
        }
    }
