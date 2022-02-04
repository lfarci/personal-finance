package be.loganfarci.financial.service.api.users;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteUserIT extends UserIT {

    @Test
    public void statusIsNoContentWhenDeletionIsSuccessful() throws Exception {
        mvc.perform(delete(getUserPathWithId(FIRST_EXISTING_USER_ID))).andExpect(status().isNoContent());
    }

    @Test
    public void statusIsNotFoundWhenIdDoesNotExist() throws Exception {
        mvc.perform(delete(getUserPathWithId(NEXT_USER_ID))).andExpect(status().isNotFound());
    }

    @Test
    public void userIsDeletedWhenDeletionIsSuccessful() throws Exception {
        mvc.perform(delete(getUserPathWithId(LAST_EXISTING_USER_ID))).andReturn();
        assertThat(repository.existsById(LAST_EXISTING_USER_ID)).isFalse();
    }

    @Test
    public void responseJsonContentIsExpectedError() throws Exception {
        String jsonContent = notFoundJsonContent(NEXT_USER_ID);
        mvc.perform(delete(getUserPathWithId(NEXT_USER_ID))).andExpect(content().json(jsonContent));
    }

}
