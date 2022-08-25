package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedDto;
import com.example.football.models.dto.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;

@Service
public class PlayerServiceImpl implements PlayerService {
    public static final String PLAYERS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Player Finder_Training\\skeleton\\src\\main\\resources\\files\\xml\\players.xml";

    private final PlayerRepository playerRepository;
    private final TownService townService;
    private final TeamService teamService;
    private final StatService statService;
    private final XMLParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PlayerServiceImpl(PlayerRepository playerRepository, TownService townService, TeamService teamService, StatService statService, XMLParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.playerRepository = playerRepository;
        this.townService = townService;
        this.teamService = teamService;
        this.statService = statService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;

    }


    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));

    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(PLAYERS_FILE_PATH, PlayerSeedRootDto.class)
                .getPlayers()
                .stream().filter(playerSeedDto -> {
                    boolean isValid = validationUtil.isValid(playerSeedDto)
                            && !isEntityExist(playerSeedDto.getEmail())
                            && townService.isEntityExist(playerSeedDto.getTown().getName())
                            && teamService.isEntityExist(playerSeedDto.getTeam().getName())
                            && statService.isEntityExist(playerSeedDto.getStat().getId());

                    sb.append(isValid ? String.format("Successfully imported Player %s %s - %s",
                                    playerSeedDto.getFirstName(), playerSeedDto.getLastName(), playerSeedDto.getPosition()) : "Invalid Player")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(playerSeedDto -> {
                    Player player = this.modelMapper.map(playerSeedDto, Player.class);
                    player.setTown(townService.findByTownName(playerSeedDto.getTown().getName()));
                    player.setTeam(teamService.findByTeamName(playerSeedDto.getTeam().getName()));
                    player.setStat(statService.findByStatId(playerSeedDto.getStat().getId()));
                    return player;
                })
                .forEach(playerRepository::save);
        return sb.toString();

    }

    ;

    private boolean isEntityExist(String email) {
        return playerRepository.existsByEmail(email);
    }

    @Override
    public String exportBestPlayers() {
        LocalDate after = LocalDate.of(1995, 1, 1);
        LocalDate before = LocalDate.of(2003, 1, 1);

        StringBuilder sb = new StringBuilder();
        this.playerRepository.findByBirthDateBetweenOrderByStatShootingDescStatPassingDescStatEnduranceDescLastNameAsc(after, before)
                .forEach(p -> {
                    sb.append(String.format("Player - %s %s\n" +
                                    "\tPosition - %s\n" +
                                    "\tTeam - %s\n" +
                                    "\tStadium - %s\n", p.getFirstName(), p.getLastName(),
                                    p.getPosition(),p.getTeam().getName(), p.getTeam().getStadiumName()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }
}
