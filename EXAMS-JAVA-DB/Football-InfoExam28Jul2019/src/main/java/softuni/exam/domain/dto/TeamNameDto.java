package softuni.exam.domain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TeamNameDto {
    private String name;
    private PictureJsonUrlDto picture;

    public TeamNameDto() {
    }

    @Size(min = 3, max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public PictureJsonUrlDto getPicture() {
        return picture;
    }

    public void setPicture(PictureJsonUrlDto picture) {
        this.picture = picture;
    }
}
