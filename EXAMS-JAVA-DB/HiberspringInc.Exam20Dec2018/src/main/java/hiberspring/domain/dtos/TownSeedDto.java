package hiberspring.domain.dtos;

import org.hibernate.validator.constraints.pl.NIP;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TownSeedDto {
    private String name;
    private Integer population;

//    public TownSeedDto() {
//    }
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Positive
    @NotNull
    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
}
