package be.loganfarci.financial.service.api.users;

import be.loganfarci.financial.service.api.users.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SaveUserIT extends UserIT {

    private static final String TOO_LONG_NAME = "Lorem ipsum dolor sit amet, consectetur vestibulum.";

    @Test
    public void save_statusIsCreatedAfterSuccessfulCreation() throws Exception {
        postUserJsonContent(toJson("User F")).andExpect(status().isCreated());
    }

    @Test
    public void save_responseContentHasExpectedIdentifier() throws Exception {
        MvcResult result = postUserJsonContent(toJson("User F")).andReturn();
        String content = result.getResponse().getContentAsString();
        UserDto response = mapper.readValue(content, UserDto.class);
        assertThat(response.getId()).isEqualTo(5L);
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsNull() throws Exception {
        postUserJsonContent(toJson(null)).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsEmpty() throws Exception {
        postUserJsonContent(toJson("")).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsBlank() throws Exception {
        postUserJsonContent(toJson("     ")).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsBadRequestWhenNameIsTooLong() throws Exception {
        postUserJsonContent(toJson(TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void save_statusIsConflictWhenSavingAnExistingUser() throws Exception {
        postUserJsonContent(toJson(0L, "User A")).andExpect(status().isConflict());
    }

    @Test
    public void save_creationDateIsEqualToLateUpdateDate() throws Exception {
        postUserJsonContent(toJson("User A")).andReturn();
        UserDto savedUser = service.findById(5L);
        assertThat(savedUser.getCreationDate()).isEqualTo(savedUser.getUpdateDate());
    }

}
