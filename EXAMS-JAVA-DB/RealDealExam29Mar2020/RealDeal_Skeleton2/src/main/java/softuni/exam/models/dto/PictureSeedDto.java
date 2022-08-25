package softuni.exam.models.dto;

import softuni.exam.models.entity.BaseEntity;
import softuni.exam.models.entity.Car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class PictureSeedDto {
    private String name;
    private String dateAndTime;
    private Long car;


    public PictureSeedDto() {
    }
    @Size(min = 2, max = 19)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
    @Positive
    public Long getCar() {
        return car;
    }

    public void setCar(Long car) {
        this.car = car;
    }
}
