package exam.model.dto;

import javax.validation.constraints.NotBlank;

public class TownNameDto {

    private String name;

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
