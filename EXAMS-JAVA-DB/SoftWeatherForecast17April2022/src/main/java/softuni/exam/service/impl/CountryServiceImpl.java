package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CountryServiceImpl implements CountryService {
    public static final String COUNTRIES_FILE_PATH = "src/main/resources/files/json/countries.json";

    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public CountryServiceImpl(CountryRepository countryRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(COUNTRIES_FILE_PATH));

    }

    @Override
    public String importCountries() throws IOException {
        // CountrySeedDto[] countrySeedDto = this.gson.fromJson(readCountriesFromFile(), CountrySeedDto[].class);
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readCountriesFromFile(), CountrySeedDto[].class))
                .filter(countrySeedDto -> {
                    boolean isValid = validationUtil.isValid(countrySeedDto)
                            && !isEntityExist(countrySeedDto.getCountryName());
                    sb.append(isValid ? String.format("Successfully imported country %s - %s",
                                    countrySeedDto.getCountryName(), countrySeedDto.getCurrency()) : "Invalid country")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(countrySeedDto -> modelMapper.map(countrySeedDto, Country.class))
                .forEach(countryRepository::save);
        return sb.toString();
    }
    @Override
    public boolean isEntityExist(String countryName) {
        return countryRepository.existsByCountryName(countryName);

    }


    @Override
    public boolean isEntityExistById(Long countryId) {
        return countryRepository.existsById(countryId);

    }

    @Override
    public Country findByCountryId(Long countryId) {
        return countryRepository.findById(countryId).orElse(null);

    }
}
