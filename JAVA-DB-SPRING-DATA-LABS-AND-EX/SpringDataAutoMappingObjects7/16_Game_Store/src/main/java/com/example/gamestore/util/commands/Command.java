package com.example.gamestore.util.commands;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

    public interface Command {
        String execute(String... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException;
    }
