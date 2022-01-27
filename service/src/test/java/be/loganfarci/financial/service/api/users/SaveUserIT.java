package be.loganfarci.financial.service.api.users;

import be.loganfarci.financial.service.api.users.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SaveUserIT extends UserIT {

    @Test
    public void save_statusIsCreatedAfterSuccessfulCreation() throws Exception {
        save(userJsonContent("User F")).andExpect(status().isCreated());
    }

    @Test
    public void save_responseContentHasExpectedIdentifier() throws Exception {
        MvcResult result = save(userJsonContent("User F")).andReturn();
        String content = result.getResponse().getContentAsString();
        UserDto response = mapper.readValue(content, UserDto.class);
        assertThat(response.getId()).isEqualTo(NEXT_USER_ID);
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsNull() throws Exception {
        save(userJsonContent(null)).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsEmpty() throws Exception {
        save(userJsonContent("")).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsBlank() throws Exception {
        save(userJsonContent("     ")).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsTooLong() throws Exception {
        save(userJsonContent(TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenLastNameIsNull() throws Exception {
        save(userJsonContent(VALID_NAME, null)).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenLastNameIsEmpty() throws Exception {
        save(userJsonContent(VALID_NAME, "")).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenLastNameIsBlank() throws Exception {
        save(userJsonContent(VALID_NAME, "     ")).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenLastNameIsTooLong() throws Exception {
        save(userJsonContent(VALID_NAME, TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsConflictWhenSavingAnExistingUser() throws Exception {
        save(userJsonContent(FIRST_EXISTING_USER_ID, "User A")).andExpect(status().isConflict());
    }

    @Test
    public void save_creationDateIsEqualToLateUpdateDate() throws Exception {
        save(userJsonContent("User A")).andReturn();
        UserDto savedUser = service.findById(NEXT_USER_ID);
        assertThat(savedUser.getCreationDate()).isEqualTo(savedUser.getUpdateDate());
    }

}
