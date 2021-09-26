package be.loganfarci.financial.service.api.owner.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OwnerDto {

    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    public OwnerDto(String name) {
        this.name = name;
    }

    public OwnerDto() { this(""); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
