package be.loganfarci.financial.service.api.users;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateUserIT extends UserIT {

    @Test
    public void updateById_statusIsNoContentAfterSuccessfulUpdate() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, "User B")).andExpect(status().isNoContent());
    }

    @Test
    public void updateById_userNameIsUpdated() throws Exception {
        String userName = "New user A";
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, userName)).andReturn();
        assertThat(service.findById(FIRST_EXISTING_USER_ID).getFirstName()).isEqualTo(userName);
    }

    @Test
    public void updateById_statusIsBadRequestWhenNameIsNull() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, null)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenNameIsEmpty() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, "")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenNameIsBlank() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, "      ")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenNameIsTooLong() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_lastNameIsUpdated() throws Exception {
        String newLastName = "New user A";
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, "User A", newLastName)).andReturn();
        assertThat(service.findById(FIRST_EXISTING_USER_ID).getLastName()).isEqualTo(newLastName);
    }

    @Test
    public void updateById_statusIsBadRequestWhenLastNameIsNull() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, "User A", null)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenLastNameIsEmpty() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, "User A","")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenLastNameIsBlank() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, "User A", "      ")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenLastNameIsTooLong() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(FIRST_EXISTING_USER_ID, "User A", TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenContentAndPathIdentifierMismatch() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(1L, "User A")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenUserIdIsNull() throws Exception {
        updateById(FIRST_EXISTING_USER_ID, userJsonContent((Long) null, "User A")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_responseContentIsExpectedErrorWhenUserIDMismatch() throws Exception {
        String jsonContent = idMismatchErrorJsonContent(1L, FIRST_EXISTING_USER_ID);
        updateById(FIRST_EXISTING_USER_ID, userJsonContent(1L, "User A")).andExpect(content().json(jsonContent));
    }

    @Test
    public void updateById_responseContentIsExpectedErrorWhenUserIdIsNull() throws Exception {
        String jsonContent = requiredIdErrorJsonContent();
        updateById(FIRST_EXISTING_USER_ID, userJsonContent((Long) null, "User A")).andExpect(content().json(jsonContent));
    }

    @Test
    public void updateById_statusIsNotFoundWhenUserDoesNotExist() throws Exception {
        updateById(NEXT_USER_ID, userJsonContent(NEXT_USER_ID, "Unknown")).andExpect(status().isNotFound());
    }

}
