package exam.model.dto;

import javax.validation.constraints.NotBlank;

public class ShopNameDto {
    private String name;

    public ShopNameDto() {
    }
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
