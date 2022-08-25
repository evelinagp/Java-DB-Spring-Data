package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.PlayerSeedDto;
import softuni.exam.domain.entities.Player;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PlayerServiceImpl implements PlayerService {
    public static final String PLAYERS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Football-Info_Training\\src\\main\\resources\\files\\json\\players.json";
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TeamService teamService, PictureService pictureService, ModelMapper modelMapper, Gson gson, ValidatorUtil validatorUtil) {
        this.playerRepository = playerRepository;
        this.teamService = teamService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public String importPlayers() throws IOException {
        //  PlayerSeedDto[] playerSeedDtos = gson.fromJson(readPlayersJsonFile(), PlayerSeedDto[].class);
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readPlayersJsonFile(), PlayerSeedDto[].class))
                .filter(playerSeedDto -> {
                    boolean isValid = validatorUtil.isValid(playerSeedDto)
                            && !(isFirstAndLastNameExists(playerSeedDto.getFirstName(), playerSeedDto.getLastName()))
                            && teamService.isEntityExist(playerSeedDto.getTeam().getName())
                            && pictureService.isEntityExist(playerSeedDto.getPicture().getUrl());

                    sb.append(isValid ? String.format("Successfully imported player: %s %s",
                                    playerSeedDto.getFirstName(), playerSeedDto.getLastName()) : "Invalid player")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(playerSeedDto -> {
                    Player player = modelMapper.map(playerSeedDto, Player.class);
                    player.setPicture(pictureService.findPicByUrl(playerSeedDto.getPicture().getUrl()));
                    player.setTeam(teamService.findTeamByName(playerSeedDto.getTeam().getName()));
                    return player;
                })
                .forEach(playerRepository::save);
        return sb.toString();
    }

    private boolean isFirstAndLastNameExists(String firstName, String lastName) {
        return this.playerRepository.existsByFirstNameAndLastName(firstName, lastName);
    }

//    private boolean isFirstNameEntityExist(String firstName) {
//        return this.playerRepository.existsByFirstName(firstName);
//
//    }
//
//    private boolean isLastNameEntityExist(String lastName) {
//        return this.playerRepository.existsByLastName(lastName);
//
//    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;

    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));

    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();
        this.playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000))
                .forEach(p -> {
                    sb.append(String.format("Player name: %s %s\n" +
                                            "\tNumber: %d\n" +
                                            "\tSalary: %.2f\n" +
                                            "\tTeam: %s\n"
                                    , p.getFirstName(), p.getLastName(),
                                    p.getNumber(), p.getSalary(), p.getTeam().getName()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder sb = new StringBuilder("Team: North Hub\n");
        this.playerRepository.findAllByTeam_NameOrderById("North Hub")
                .forEach(p -> {
                    sb.append(String.format("Player name: %s %s - %s\n" +
                                            "Number: %d"
                                    , p.getFirstName(), p.getLastName(),
                                    p.getPosition(), p.getNumber()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }
}
