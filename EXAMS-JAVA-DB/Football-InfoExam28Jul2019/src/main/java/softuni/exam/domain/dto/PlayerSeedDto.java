package softuni.exam.domain.dto;


import softuni.exam.domain.entities.Position;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;


public class PlayerSeedDto {
    private String firstName;
    private String lastName;
    private Integer number;
    private BigDecimal salary;
    private Position position;
    private PictureJsonUrlDto picture;
    private TeamNameDto team;

    public PlayerSeedDto() {
    }

    @NotBlank
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Size(min = 3, max = 20)
    @NotBlank
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Min(1)
    @Max(99)
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @PositiveOrZero
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    public PictureJsonUrlDto getPicture() {
        return picture;
    }

    public void setPicture(PictureJsonUrlDto picture) {
        this.picture = picture;
    }

    public TeamNameDto getTeam() {
        return team;
    }

    public void setTeam(TeamNameDto team) {
        this.team = team;
    }
}
