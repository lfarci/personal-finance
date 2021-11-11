package be.loganfarci.financial.service.api.users;

import be.loganfarci.financial.service.api.users.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindUserIT extends UserIT {

    @Test
    public void findById_statusIsOkWhenIdExists() throws Exception {
        mvc.perform(get("/api/users/0")).andExpect(status().isOk());
    }

    @Test
    public void findById_responseJsonContentIsExpectedUser() throws Exception {
        String jsonContent = toJson(0L, "User A");
        mvc.perform(get("/api/users/0")).andExpect(content().json(jsonContent));
    }

    @Test
    public void findById_statusIsNotFoundWhenIdDoesNotExist() throws Exception {
        mvc.perform(get("/api/users/5")).andExpect(status().isNotFound());
    }

    @Test
    public void findById_responseJsonContentIsExpectedError() throws Exception {
        String jsonContent = getUserNotFoundJsonContent();
        mvc.perform(get("/api/users/5")).andExpect(content().json(jsonContent));
    }

    @Test
    public void findAll_statusIsOk() throws Exception {
        mvc.perform(get("/api/users")).andExpect(status().isOk());
    }

    @Test
    public void findAll_responseContentHasExpectedLength() throws Exception {
        MvcResult result = mvc.perform(get("/api/users")).andReturn();
        String json = result.getResponse().getContentAsString();
        UserDto[] users = mapper.readValue(json, UserDto[].class);
        assertThat(users).hasSize(5);
    }

    @Test
    public void findAll_responseContentHasExpectedUsers() throws Exception {
        MvcResult result = mvc.perform(get("/api/users")).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(makeUsersJsonOfSize(5));
    }

}