package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastSeedRootDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ForecastServiceImpl implements ForecastService {
    public static final String FORECASTS_FILE_PATH = "src/main/resources/files/xml/forecasts.xml";

    private final ForecastRepository forecastRepository;
    private final CityService cityService;
    private final XMLParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public ForecastServiceImpl(ForecastRepository forecastRepository, CityService cityService, XMLParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.forecastRepository = forecastRepository;
        this.cityService = cityService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECASTS_FILE_PATH));

    }
    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
       xmlParser.fromFile(FORECASTS_FILE_PATH, ForecastSeedRootDto.class)
                .getForecasts()
                .stream().filter(forecastSeedDto -> {
                    boolean isValid = validationUtil.isValid(forecastSeedDto)
                            && !(isEntityExistByDayAndByCityId(forecastSeedDto.getDaysOfWeek(), forecastSeedDto.getCity()))
                            && cityService.isEntityExistById(forecastSeedDto.getCity());
                    sb.append(isValid ? String.format("Successfully imported forecast %s - %.2f",
                                    forecastSeedDto.getDaysOfWeek(), forecastSeedDto.getMaxTemperature()) : "Invalid forecast")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(forecastSeedDto -> {
                    Forecast forecast = modelMapper.map(forecastSeedDto, Forecast.class);
                    forecast.setCity(cityService.findByCityId(forecastSeedDto.getCity()));
                    return forecast;
                })
                .forEach(forecastRepository::save);
        return sb.toString();
    }

    private boolean isEntityExistByDayAndByCityId(DaysOfWeek daysOfWeek, Long cityId) {
        return forecastRepository.existsByCity_IdAndDaysOfWeek(cityId, daysOfWeek);

    }


    @Override
    public String exportForecasts() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        this.forecastRepository.findByDaysOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(DaysOfWeek.SUNDAY, 150000)
                .forEach(f-> {
               sb.append(String.format("City: %s:\n" +
                       "\t-min temperature: %.2f\n" +
                       "\t--max temperature: %.2f\n" +
                       "\t---sunrise: %s\n" +
                       "\t----sunset: %s\n", f.getCity().getCityName(), f.getMinTemperature(),
                       f.getMaxTemperature(), f.getSunrise(), f.getSunset()));
                });
        return sb.toString();
    }


}
