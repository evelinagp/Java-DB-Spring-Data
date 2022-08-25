package softuni.exam.service;


import softuni.exam.models.dto.AgentNameDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;

import java.io.IOException;

// TODO: Implement all methods
public interface AgentService {

    boolean areImported();

    String readAgentsFromFile() throws IOException;
	
	String importAgents() throws IOException;

    Agent findByAgentName(String agentFirstName);

    boolean isEntityExists(String firstName);

    boolean isEntityExistsByEmail(String email);
}
