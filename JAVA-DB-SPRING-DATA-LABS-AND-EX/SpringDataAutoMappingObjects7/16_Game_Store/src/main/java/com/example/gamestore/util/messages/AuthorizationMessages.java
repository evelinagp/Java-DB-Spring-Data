package com.example.gamestore.util.messages;

public class AuthorizationMessages {

        public static String INVALID_USERNAME = "Username must be at least 1 character long.";
        public static String INVALID_EMAIL = "Invalid email.";
        public static String INVALID_PASSWORD = "Password must be 6 characters long and contain at least 1 uppercase, 1 lowercase letter and 1 digit.";
        public static String EMAIL_INUSE = "There is an existing user with that email.";
        public static String REGISTER_SUCCESS = "%s was registered.";


        public static String USER_ALREADY_LOGGED = "A user is already logged in.";
        public static String LOGIN_WRONG_CREDENTIALS = "Invalid password/username.";
        public static String LOGIN_SUCCESS = "Successfully logged in %s.";


        public static String LOGOUT_SUCCESS = "Successfully logged out %s.";
        public static String LOGOUT_NO_USER_LOGGED = "Cannot log out. No user was logged in.";
    }

