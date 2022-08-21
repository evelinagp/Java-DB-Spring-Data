package com.example.gamestore;

import com.example.gamestore.util.io.InputHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final InputHandler inputHandler;

    public ConsoleRunner(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void run(String... args) throws Exception {

        //implements console login/reg/logout service
        //runs until you type END
        //Keep in mind that only the first account to be registered
        //will have admin privileges of adding games etc.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write your commands:");
        String commandLine;

        while (!(commandLine = scanner.nextLine()).equals("END")) {
            System.out.println(this.inputHandler.executeInput(commandLine));
        }
    }
}