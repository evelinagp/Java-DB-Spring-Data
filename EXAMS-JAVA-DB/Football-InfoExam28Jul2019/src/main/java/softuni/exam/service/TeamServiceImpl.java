package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.XmlParser;
import softuni.exam.domain.dto.TeamSeedRootDto;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.ValidatorUtil;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TeamServiceImpl implements TeamService {
    public static final String TEAMS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Football-Info_Training\\src\\main\\resources\\files\\xml\\teams.xml";

    private final TeamRepository teamRepository;
    private final PictureService pictureService;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validationUtil;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PictureService pictureService, XmlParser xmlParser, ModelMapper modelMapper, ValidatorUtil validationUtil) {
        this.teamRepository = teamRepository;
        this.pictureService = pictureService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        //   PictureSeedRootDto pictureSeedRootDto = xmlParser.fromFile(PICTURES_FILE_PATH, PictureSeedRootDto.class);/*.getPictures().stream()*/
        xmlParser.fromFile(TEAMS_FILE_PATH, TeamSeedRootDto.class).getTeams().stream()
                .filter(teamSeedDto -> {
                    boolean isValid = validationUtil.isValid(teamSeedDto)
                            && !isEntityExist(teamSeedDto.getName())
                            && pictureService.isEntityExist(teamSeedDto.getPicture().getUrl());
                    sb.append(isValid
                                    ? String.format("Successfully imported - %s",
                                    teamSeedDto.getName()) : "Invalid team")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(teamSeedDto -> {
                    Team team = modelMapper.map(teamSeedDto, Team.class);
                    team.setPicture(pictureService.findPicByUrl(teamSeedDto.getPicture().getUrl()));
                    return team;
                })
                .forEach(teamRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEntityExist(String name) {
        return this.teamRepository.existsByName(name);

    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;

    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files.readString(Path.of(TEAMS_FILE_PATH));

    }

    @Override
    public Team findTeamByName(String name) {
        return this.teamRepository.findByName(name).orElse(null);

    }
}
