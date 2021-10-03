package be.loganfarci.financial.service.api.category.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TransactionCategory {

    private @NotBlank @Size(max = 50) String name;
    private @Size(max = 255) String description;
    private @Size(max = 50) String parentName;

    public TransactionCategory(String name, String description, String parentName) {
        this.name = name;
        this.description = description;
        this.parentName = parentName;
    }

    public TransactionCategory() {
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

    public String getParentName() {
        return parentName;
    }

    public void setParent(String parentName) {
        this.parentName = parentName;
    }
}
