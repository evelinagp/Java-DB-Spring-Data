package com.example.gamestore.services.validators;

import com.example.gamestore.util.messages.AuthorizationMessages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidator {
    private static boolean isValidFullName(String fullName) {
        return !fullName.isEmpty();
    }

    private static boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6 || !password.equals(confirmPassword)) {
            return false;
        }

        String lowerUpperCheck = "^(?=[A-Z]*[a-z])(?=[a-z]*[A-Z])[a-zA-Z0-9]+$";
        String digitCheck = "[0-9]+";

        Pattern lowerUpperPattern = Pattern.compile(lowerUpperCheck);
        Pattern digitPattern = Pattern.compile(digitCheck);

        Matcher caseMatcher = lowerUpperPattern.matcher(password);
        Matcher digitMatcher = digitPattern.matcher(password);

        return  caseMatcher.find() && digitMatcher.find();
    }

    private static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("\\w+@[a-zA-Z]+\\.[a-zA-Z]+");
        Matcher emailMatcher = emailPattern.matcher(email);

        return emailMatcher.find();
    }

    public static void validateCredentials(String email, String fullName, String password, String confirmPassword) {
        if (!RegisterValidator.isValidEmail(email)) {
            throw new IllegalArgumentException(AuthorizationMessages.INVALID_EMAIL);
        }

        if (!RegisterValidator.isValidFullName(fullName)) {
            throw new IllegalArgumentException(String.format(AuthorizationMessages.INVALID_USERNAME, fullName));
        }

        if (!RegisterValidator.isValidPassword(password, confirmPassword)) {
            throw new IllegalArgumentException(AuthorizationMessages.INVALID_PASSWORD);
        }
    }
}