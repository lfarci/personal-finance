package be.loganfarci.financial.service.api.users;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateUserIT extends UserIT {

    private static final String TOO_LONG_NAME = "Lorem ipsum dolor sit amet, consectetur vestibulum.";

    @Test
    public void updateById_statusIsNoContentAfterSuccessfulUpdate() throws Exception {
        putUserJsonContent(0L, toJson(0L, "User A")).andExpect(status().isNoContent());
    }

    @Test
    public void updateById_userNameIsUpdated() throws Exception {
        String userName = "New user A";
        putUserJsonContent(0L, toJson(0L, userName)).andReturn();
        assertThat(service.findById(0L).getName()).isEqualTo(userName);
    }

    @Test
    public void updateById_statusIsBadRequestWhenNameIsNull() throws Exception {
        putUserJsonContent(0L, toJson(0L, null)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenNameIsEmpty() throws Exception {
        putUserJsonContent(0L, toJson(0L, "")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenNameIsBlank() throws Exception {
        putUserJsonContent(0L, toJson(0L, "      ")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenNameIsTooLong() throws Exception {
        putUserJsonContent(0L, toJson(0L, TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenContentAndPathIdentifierMismatch() throws Exception {
        putUserJsonContent(0L, toJson(1L, "User A")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_statusIsBadRequestWhenUserIdIsNull() throws Exception {
        putUserJsonContent(0L, toJson((Long) null, "User A")).andExpect(status().isBadRequest());
    }

    @Test
    public void updateById_responseContentIsExpectedErrorWhenUserIDMismatch() throws Exception {
        String title = getMessage("title.bad_request");
        String message = getMessage("user.id_mismatch", new Long[] { 1L, 0L });
        String jsonContent = toJson(title, message);
        putUserJsonContent(0L, toJson(1L, "User A")).andExpect(content().json(jsonContent));
    }

    @Test
    public void updateById_responseContentIsExpectedErrorWhenUserIdIsNull() throws Exception {
        String title = getMessage("title.bad_request");
        String message = getMessage("user.required_id");
        String jsonContent = toJson(title, message);
        putUserJsonContent(0L, toJson((Long) null, "User A")).andExpect(content().json(jsonContent));
    }

    @Test
    public void updateById_statusIsNotFoundWhenUserDoesNotExist() throws Exception {
        putUserJsonContent(5L, toJson(5L, "Unknown")).andExpect(status().isNotFound());
    }

}
