package be.loganfarci.financial.service.api.users;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteUserIT extends UserIT {

    @Test
    public void deleteById_statusIsNoContentWhenDeletionIsSuccessful() throws Exception {
        mvc.perform(delete("/api/users/0")).andExpect(status().isNoContent());
    }

    @Test
    public void deleteById_statusIsNotFoundWhenIdDoesNotExist() throws Exception {
        mvc.perform(delete("/api/users/5")).andExpect(status().isNotFound());
    }

    @Test
    public void deleteById_userIsDeleted() throws Exception {
        mvc.perform(delete("/api/users/4")).andReturn();
        assertThat(repository.existsById(4L)).isFalse();
    }

    @Test
    public void deleteById_responseJsonContentIsExpectedError() throws Exception {
        String jsonContent = getUserNotFoundJsonContent();
        mvc.perform(delete("/api/users/5")).andExpect(content().json(jsonContent));
    }

}
