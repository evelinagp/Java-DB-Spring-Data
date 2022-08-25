package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {
    public static final String AGENTS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Real Estate Agency_Skeleton3\\src\\main\\resources\\files\\json\\agents.json";
    private final AgentRepository agentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public AgentServiceImpl(AgentRepository agentRepository, TownService townService, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.agentRepository = agentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;

    }


    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENTS_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder sb = new StringBuilder();
//        AgentSeedDto[] agentSeedDtos = this.gson.fromJson(readAgentsFromFile(), AgentSeedDto[].class);
//        return null;

        Arrays.stream(gson.fromJson(readAgentsFromFile(), AgentSeedDto[].class))
                .filter(agentSeedDto -> {
                    boolean isValid = validationUtil.isValid(agentSeedDto)
                            && !isEntityExists(agentSeedDto.getFirstName()) && !isEntityExistsByEmail(agentSeedDto.getEmail())
                            && townService.isEntityExist(agentSeedDto.getTown());
                    sb.append(isValid ? String.format("Successfully imported agent - %s %s",
                                    agentSeedDto.getFirstName(), agentSeedDto.getLastName()) : "Invalid agent")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(agentSeedDto -> {
                    Agent agent = this.modelMapper.map(agentSeedDto, Agent.class);
                    agent.setTown(townService.findByTownName(agentSeedDto.getTown()));
                    return agent;
                })
                .forEach(agentRepository::save);
        return sb.toString();

    }

    @Override
    public Agent findByAgentName(String agentFirstName) {
        return agentRepository.findByFirstName(agentFirstName).orElse(null);

    }

    @Override
    public boolean isEntityExists(String firstName) {
        return agentRepository.existsAgentByFirstName(firstName);
    }

    @Override
    public boolean isEntityExistsByEmail(String email) {
        return agentRepository.existsAgentByEmail(email);
    }

}



