package com.example.jsonprocessingex9.util.exporters;

import com.example.jsonprocessingex9.util.parsers.Parser;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileDataWriter {
    private final String EXPORTS_DIRECTORY_PATH = "src\\main\\resources\\exports\\";
    private final String JSON_EXTENSION = ".json";
    private final String XML_EXTENSION = ".xml";
    Parser parser;

    public FileDataWriter(Parser parser) {
        this.parser = parser;
    }

    public void exportDataToJsonFile(String filename, Object data) throws IOException {
        File file = this.createResourceFile(filename, JSON_EXTENSION);
        String dataString = this.parser.toJson(data);

        this.writeData(dataString, file);
    }

    public <T> void exportDataToXmlFile(String filename, T entity) throws IOException, JAXBException {
        File file = this.createResourceFile(filename, XML_EXTENSION);
        String dataString = this.parser.toXML(entity);

        this.writeData(dataString, file);
    }

    private void writeData(String data, File file) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
            output.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createResourceFile(String filename, String extension) throws IOException {
        String path = this.createFilePath(filename, extension);
        File file = new File(path);

        if (file.exists()) {
            throw new IllegalArgumentException("File with that name already exists.");
        }
        file.createNewFile();

        return file;
    }

    private String createFilePath(String filename, String extension) {
        return String.format("%s%s%s", EXPORTS_DIRECTORY_PATH, filename, extension);
    }
}