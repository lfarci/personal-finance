package be.loganfarci.financial.service.api.category.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class TransactionCategoryDto {

    private @NotBlank @Size(max = 50) String name;
    private @Null @Size(max = 255) String description;
    private @NotBlank @Size(max = 50) TransactionCategoryDto parent;

    public TransactionCategoryDto(String name, String description, @NotBlank @Size(max = 50) TransactionCategoryDto parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
    }

    public TransactionCategoryDto() {
        this(null, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotBlank @Size(max = 50) TransactionCategoryDto getParent() {
        return parent;
    }

    public void setParent(@NotBlank @Size(max = 50) TransactionCategoryDto parent) {
        this.parent = parent;
    }
}
