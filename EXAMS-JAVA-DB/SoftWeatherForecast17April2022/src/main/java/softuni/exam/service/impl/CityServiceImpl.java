package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CitySeedDto;
import softuni.exam.models.entity.City;
import softuni.exam.repository.CityRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CityServiceImpl implements CityService {
    public static final String CITIES_FILE_PATH = "src/main/resources/files/json/cities.json";

    private final CityRepository cityRepository;
    private final CountryService countryService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public CityServiceImpl(CityRepository cityRepository, CountryService countryService, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.cityRepository = cityRepository;
        this.countryService = countryService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }


    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITIES_FILE_PATH));

    }

    @Override
    public String importCities() throws IOException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readCitiesFileContent(), CitySeedDto[].class))
                .filter(citySeedDto -> {
                    boolean isValid = validationUtil.isValid(citySeedDto)
                            && !isEntityExist(citySeedDto.getCityName())
                            && countryService.isEntityExistById(citySeedDto.getCountry());
                    sb.append(isValid ? String.format("Successfully imported city %s - %d",
                                    citySeedDto.getCityName(), citySeedDto.getPopulation()) : "Invalid city")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(citySeedDto -> {
                    City city = modelMapper.map(citySeedDto, City.class);
                    city.setCountry(countryService.findByCountryId(citySeedDto.getCountry()));
                    return city;
                })
                .forEach(cityRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEntityExistById(Long cityId) {
        return cityRepository.existsById(cityId);

    }

    @Override
    public City findByCityId(Long cityId) {
        return cityRepository.findById(cityId).orElse(null);

    }

    private boolean isEntityExist(String cityName) {
        return cityRepository.existsByCityName(cityName);

    }
}
