package be.loganfarci.financial.service.api.users.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class UserDto {

    @Min(0)
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    public UserDto(Long id, String name, LocalDateTime creationDate, LocalDateTime updateDate) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
