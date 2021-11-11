package be.loganfarci.financial.service.api.users;

import be.loganfarci.financial.service.api.users.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SaveUserIT extends UserIT {

    @Test
    public void save_statusIsCreatedAfterSuccessfulCreation() throws Exception {
        save(toJson("User F")).andExpect(status().isCreated());
    }

    @Test
    public void save_responseContentHasExpectedIdentifier() throws Exception {
        MvcResult result = save(toJson("User F")).andReturn();
        String content = result.getResponse().getContentAsString();
        UserDto response = mapper.readValue(content, UserDto.class);
        assertThat(response.getId()).isEqualTo(5L);
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsNull() throws Exception {
        save(toJson(null)).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsEmpty() throws Exception {
        save(toJson("")).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsBlank() throws Exception {
        save(toJson("     ")).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsTooLong() throws Exception {
        save(toJson(TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsConflictWhenSavingAnExistingUser() throws Exception {
        save(toJson(FIRST_EXISTING_USER_ID, "User A")).andExpect(status().isConflict());
    }

    @Test
    public void save_creationDateIsEqualToLateUpdateDate() throws Exception {
        save(toJson("User A")).andReturn();
        UserDto savedUser = service.findById(NEXT_USER_ID);
        assertThat(savedUser.getCreationDate()).isEqualTo(savedUser.getUpdateDate());
    }

}
