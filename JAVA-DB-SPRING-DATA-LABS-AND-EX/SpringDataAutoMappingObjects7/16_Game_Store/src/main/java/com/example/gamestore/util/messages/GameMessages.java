package com.example.gamestore.util.messages;

public class GameMessages {

        public static String INVALID_TITLE = "Title must begin with an uppercase letter and must have length between 3 and 100 symbols.";
        public static String INVALID_PRICE = "Price must be a positive number with precision up to 2 digits after the floating point.";
        public static String INVALID_SIZE = "Size must be a positive number with precision up to 1 digit after the floating point.";
        public static String INVALID_TRAILER = "Trailer must be a youtube URL.";
        public static String INVALID_THUMBNAIL = "Invalid thumbnail URL.";
        public static String INVALID_DESCRIPTION = "Description must be at least 20 symbols.";
        public static String INVALID_DATE = "Date must be in the format dd-MM-yyyy.";
        public static String TITLE_EXISTS = "Game with title: %s already exists.";


        public static String NO_ADMIN_LOGGED = "Please login into your admin account to add games.";
        public static String NO_USER_LOGGED = "Please login into your account.";
        public static String UNAUTHORIZED = "Only Admin accounts allowed to add games!";


        public static String VALID_GAME = "Valid.";
        public static String SUCC_ADDED_GAME = "Added %s";

        public static String DELETE_INVALID_ID = "Invalid game ID.";
        public static String DELETE_ID_NOT_EXIST = "No game with such ID exists.";
        public static String DELETE_SUCCESS = "Successfully deleted game.";


        public static String MISSING_TITLE = "Missing game title.";
    }

