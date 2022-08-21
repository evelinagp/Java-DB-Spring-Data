package com.example.gamestore.util.commands;

import com.example.gamestore.services.AuthenticationService;
import com.example.gamestore.services.AuthenticationServiceImpl;

@Cmd
public class RegisterCommand implements Command {
    private AuthenticationService authenticationService;

    public RegisterCommand(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public String execute(String... args) {
        try {
            return this.authenticationService.register(args[0], args[1], args[2], args[3]);
        }catch (ArrayIndexOutOfBoundsException e) {
            return "Fill all fields - Username, password, email to register.";
        }
    }
}
