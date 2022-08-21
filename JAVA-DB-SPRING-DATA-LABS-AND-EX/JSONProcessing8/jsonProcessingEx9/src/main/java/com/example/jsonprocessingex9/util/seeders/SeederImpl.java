package com.example.jsonprocessingex9.util.seeders;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public abstract class SeederImpl implements Seeder {

    protected String getFileContent(String resourceFilename) throws IOException {
        File file = new ClassPathResource("imports\\" + resourceFilename).getFile();
        BufferedReader bfr = new BufferedReader(new java.io.FileReader(file));
        StringBuilder content = new StringBuilder();

        String line;
        while ((line = bfr.readLine()) != null) {
            content.append(line);
        }

        return content.toString();
    }
}