package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PassengerSeedDto;
import softuni.exam.models.entity.Passenger;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {
    public static final String PASSENGERS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Airline_Skeleton_Training\\src\\main\\resources\\files\\json\\passengers.json";

    private final PassengerRepository passengerRepository;
    private final TownService townService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PassengerServiceImpl(PassengerRepository passengerRepository, TownService townService, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.passengerRepository = passengerRepository;
        this.townService = townService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return passengerRepository.count() > 0;

    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_FILE_PATH));

    }

    @Override
    public String importPassengers() throws IOException {
        //PictureSeedDto[] pictureSeedDtos = gson.fromJson(readPicturesFromFile(), PictureSeedDto[].class);
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readPassengersFileContent(), PassengerSeedDto[].class))
                .filter(passengerSeedDto -> {
                    boolean isValid = validationUtil.isValid(passengerSeedDto)
                            && !isEntityExists(passengerSeedDto.getEmail())
                            && townService.isEntityExist(passengerSeedDto.getTown());
                    sb.append(isValid ? String.format("Successfully imported Passenger %s - %s",
                                    passengerSeedDto.getLastName(), passengerSeedDto.getEmail()) : "Invalid Passenger ")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(passengerSeedDto -> {
                    Passenger passenger = modelMapper.map(passengerSeedDto, Passenger.class);
                    passenger.setTown(townService.findByTownName(passengerSeedDto.getTown()));
                    return passenger;
                })
                .forEach(passengerRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEntityExists(String email) {
        return this.passengerRepository.existsByEmail(email);
    }

    @Override
    public Passenger findPassengerByEmail(String email) {
        return this.passengerRepository.findByEmail(email);
    }


    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();

        this.passengerRepository.findPassengersByTicketTicketsCountDescendingThenByEmail()
                .forEach(passenger -> {
                    sb.append(String.format("Passenger %s  %s\n" +
                                            "\tEmail - %s\n" +
                                            "\tPhone - %s\n" +
                                            "\tNumber of tickets - %d\n", passenger.getFirstName(), passenger.getLastName(),
                                    passenger.getEmail(), passenger.getPhoneNumber(), passenger.getTickets().size()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }


}
