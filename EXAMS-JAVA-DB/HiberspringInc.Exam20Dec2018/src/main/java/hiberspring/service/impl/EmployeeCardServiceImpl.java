package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.EmployeeCardSeedDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    public static final String CARDS_FILE_PATH = Constants.PATH_TO_FILES + "employee-cards.json";

    private final EmployeeCardRepository employeeCardRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.employeeCardRepository = employeeCardRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count() > 0;

    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return Files.readString(Path.of(CARDS_FILE_PATH));

    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readEmployeeCardsJsonFile(), EmployeeCardSeedDto[].class))
                .filter(employeeCardSeedDto -> {
                    boolean isValid = validationUtil.isValid(employeeCardSeedDto)
                            && !isEntityExist(employeeCardSeedDto.getNumber());
                    sb.append(isValid ? String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                                    EmployeeCard.class.getSimpleName(), employeeCardSeedDto.getNumber()) : Constants.INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                }).map(employeeCardSeedDto -> modelMapper.map(employeeCardSeedDto, EmployeeCard.class))
                .forEach(employeeCardRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEntityExist(String number) {
        return employeeCardRepository.existsByNumber(number);

    }

    @Override
    public EmployeeCard findByCardNumber(String cardNumber) {
        return employeeCardRepository.findByNumber(cardNumber).orElse(null);

    }
}
