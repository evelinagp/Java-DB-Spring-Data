package com.example.gamestore.util.io;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

    public interface InputHandler {
        String executeInput(String input) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException;
    }