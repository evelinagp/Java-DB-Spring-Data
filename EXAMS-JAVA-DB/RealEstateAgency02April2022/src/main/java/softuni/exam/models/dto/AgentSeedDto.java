package softuni.exam.models.dto;

import softuni.exam.models.entity.Town;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AgentSeedDto {
    @Size(min = 2)
    @Column(unique = true)
    private String firstName;
    @Size(min = 2)
    private String lastName;

    private String town;
    @Email
    @Column(unique = true)
    private String email;

    public AgentSeedDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
