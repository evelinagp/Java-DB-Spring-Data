package softuni.exam.service;

import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;

import javax.xml.bind.JAXBException;
import java.io.IOException;

// TODO: Implement all methods
public interface ApartmentService {
    
    boolean areImported();

    String readApartmentsFromFile() throws IOException;

    String importApartments() throws IOException, JAXBException;


    boolean isEntityExistByArea(double area);

    boolean isEntityExist(String town);

    Apartment findById(Long id);

    boolean isEntityExists(Long id);
}
