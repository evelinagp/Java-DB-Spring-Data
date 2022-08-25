package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.TownSeedDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TownServiceImpl implements TownService {
    public static final String TOWNS_FILE_PATH = Constants.PATH_TO_FILES + "towns.json";

    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;

    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));

    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {
        //  TownSeedDto[] townSeedDtos = this.gson.fromJson(readTownsJsonFile(), TownSeedDto[].class);
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readTownsJsonFile(), TownSeedDto[].class))
                .filter(townSeedDto -> {

                    boolean isValid = validationUtil.isValid(townSeedDto)
                            && !isEntityExist(townSeedDto.getName());
                    sb.append(isValid ? String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                                   Town.class.getSimpleName(), townSeedDto.getName()) : Constants.INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                }).map(townSeedDto -> modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEntityExist(String name) {
        return townRepository.existsByName(name);

    }

    @Override
    public Town findByTownName(String townName) {
        return townRepository.findByName(townName).orElse(null);
    }
}
