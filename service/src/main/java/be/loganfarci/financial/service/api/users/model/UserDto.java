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
    private String firstName;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    public UserDto(Long id, String firstName, String lastName, LocalDateTime creationDate, LocalDateTime updateDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
