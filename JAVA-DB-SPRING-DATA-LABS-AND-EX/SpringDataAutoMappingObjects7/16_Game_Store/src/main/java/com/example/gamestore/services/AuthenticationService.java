package com.example.gamestore.services;

public interface AuthenticationService {
    String register(String email, String password, String confirmPassword, String fullName);

    String login(String email, String password);

    String logout();
}
