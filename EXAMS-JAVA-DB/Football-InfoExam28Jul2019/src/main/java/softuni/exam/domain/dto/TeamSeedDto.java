package softuni.exam.domain.dto;

import softuni.exam.domain.entities.Picture;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSeedDto {
    @XmlElement()
    private String name;
    @XmlElement()
    private PictureUrlDto picture;

    @Size(min = 3, max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @NotNull
    public PictureUrlDto getPicture() {
        return picture;
    }

    public void setPicture(PictureUrlDto picture) {
        this.picture = picture;
    }
}
