package be.loganfarci.financial.service.api.users;

import be.loganfarci.financial.service.api.users.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindUserIT extends UserIT {

    @Test
    public void findById_statusIsOkWhenIdExists() throws Exception {
        findById(FIRST_EXISTING_USER_ID).andExpect(status().isOk());
    }

    @Test
    public void findById_responseJsonContentIsExpectedUser() throws Exception {
        String jsonContent = userJsonContent(FIRST_EXISTING_USER_ID, "User A", "User A");
        findById(FIRST_EXISTING_USER_ID).andExpect(content().json(jsonContent));
    }

    @Test
    public void findById_statusIsNotFoundWhenIdDoesNotExist() throws Exception {
        findById(NEXT_USER_ID).andExpect(status().isNotFound());
    }

    @Test
    public void findById_responseJsonContentIsExpectedError() throws Exception {
        String jsonContent = notFoundJsonContent(NEXT_USER_ID);
        findById(NEXT_USER_ID).andExpect(content().json(jsonContent));
    }

    @Test
    public void statusIsOkWhenGettingUsers() throws Exception {
        findAll().andExpect(status().isOk());
    }

    @Test
    public void responseContentHasExpectedLengthWhenGettingUsers() throws Exception {
        MvcResult result = findAll().andReturn();
        String json = result.getResponse().getContentAsString();
        UserDto[] users = mapper.readValue(json, UserDto[].class);
        assertThat(users).hasSize(5);
    }

    @Test
    public void responseContentHasExpectedUsersWhenGettingUsers() throws Exception {
        MvcResult result = findAll().andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(makeUsersJsonOfSize(5));
    }

}