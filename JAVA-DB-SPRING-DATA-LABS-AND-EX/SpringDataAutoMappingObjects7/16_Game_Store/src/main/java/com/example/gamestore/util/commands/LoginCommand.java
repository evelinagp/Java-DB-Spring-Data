package com.example.gamestore.util.commands;

import com.example.gamestore.services.AuthenticationService;
import com.example.gamestore.services.AuthenticationServiceImpl;

@Cmd
public class LoginCommand implements Command {
    private AuthenticationService authenticationService;

    public LoginCommand(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public String execute(String... args) {
        try {
            return this.authenticationService.login(args[0], args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Need email and password to login!";
        }
    }
}
