package hiberspring.domain.dtos;

import javax.validation.constraints.NotNull;


public class BranchSeedDto {
    private String name;
    private String town;

    public BranchSeedDto() {
    }
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
