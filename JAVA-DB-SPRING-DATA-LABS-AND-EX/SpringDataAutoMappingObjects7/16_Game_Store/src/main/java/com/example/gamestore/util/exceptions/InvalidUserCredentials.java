package com.example.gamestore.util.exceptions;

public class InvalidUserCredentials extends Exception {
    public InvalidUserCredentials(String message) {
        super(message);
    }

    public InvalidUserCredentials(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserCredentials(Throwable cause) {
        super(cause);
    }

    public InvalidUserCredentials() {
    }
}
