package softuni.exam.service;


import softuni.exam.models.entity.Country;

import java.io.IOException;
import java.util.Optional;


public interface CountryService {

    boolean areImported();

    String readCountriesFromFile() throws IOException;
	
	String importCountries() throws IOException;

    boolean isEntityExist(String countryName);

    boolean isEntityExistById(Long countryId);

    Country findByCountryId(Long countryId);
}
