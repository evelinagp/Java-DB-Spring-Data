package com.example.football.service;

import com.example.football.models.entity.Team;

import java.io.IOException;

//ToDo - Implement all methods
public interface TeamService {
    boolean areImported();

    String readTeamsFileContent() throws IOException;

    String importTeams() throws IOException;

    boolean isEntityExist(String name);

    Team findByTeamName(String name);
}