package com.example.football.models.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class TeamSeedDto {
    private String name;
    private String stadiumName;
    private String history;
    private int fanBase;
    private String townName;

    public TeamSeedDto() {
    }

    @Size(min = 3)
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(min = 3)
    @NotBlank
    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    @Size(min = 10)
    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @Min(1000)
    public int getFanBase() {
        return fanBase;
    }

    public void setFanBase(int fanBase) {
        this.fanBase = fanBase;
    }

    @NotBlank
    public String getTown() {
        return townName;
    }

    public void setTown(String townName) {
        this.townName = townName;
    }
}
