package com.example.football.service.impl;

import com.example.football.models.dto.TownSeedDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;


@Service
public class TownServiceImpl implements TownService {
    public static final String TOWNS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Player Finder_Training\\skeleton\\src\\main\\resources\\files\\json\\towns.json";

    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        // TownSeedDto[] townSeedDtos = this.gson.fromJson(readTownsFileContent(), TownSeedDto[].class);
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readTownsFileContent(), TownSeedDto[].class))
                .filter(townSeedDto -> {
                    boolean isValid = validationUtil.isValid(townSeedDto)
                            && !isEntityExist(townSeedDto.getName());
                    sb.append(isValid ? String.format("Successfully imported Town %s - %d",
                                    townSeedDto.getName(), townSeedDto.getPopulation()) : "Invalid Town")
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
    public Town findByTownName(String name) {
        return townRepository.findByName(name).orElse(null);

    }
}
