package softuni.exam.service;

import softuni.exam.domain.entities.Picture;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface PictureService {
    String importPictures() throws JAXBException, FileNotFoundException;

    boolean isEntityExist(String url);

    boolean areImported();

    String readPicturesXmlFile() throws IOException;

    Picture findPicByUrl(String url);
}
