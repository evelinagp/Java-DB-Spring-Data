package exam.service;



import exam.model.dto.TownNameDto;
import exam.model.entity.Town;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface TownService {

    boolean areImported();

    String readTownsFileContent() throws IOException;
	
	String importTowns() throws JAXBException, FileNotFoundException;

    boolean isEntityExist(String name);

    Town findTownByName(String name);


}
