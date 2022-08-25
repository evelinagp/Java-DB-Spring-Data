package hiberspring.domain.dtos;

import hiberspring.domain.entities.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


public class EmployeeCardSeedDto {
    @Column(unique = true)
    private String number;

    public EmployeeCardSeedDto() {
    }
    @NotNull
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
