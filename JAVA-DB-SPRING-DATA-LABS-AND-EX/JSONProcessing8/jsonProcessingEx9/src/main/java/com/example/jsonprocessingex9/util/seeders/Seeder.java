package com.example.jsonprocessingex9.util.seeders;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface Seeder {
    void SeedDbFromJson(String resourceFilename) throws IOException;

    void SeedDbFromXML(String resourceFilename) throws IOException, JAXBException;
}