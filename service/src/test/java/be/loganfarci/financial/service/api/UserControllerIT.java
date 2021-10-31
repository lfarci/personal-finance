package be.loganfarci.financial.service.api;

import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.persistence.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@Sql(scripts = "classpath:users.sql")
@Transactional
public class UserControllerIT {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_TIME = "2021-01-01 00:00:00";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private MessageSource messageSource;

    public static char getChar(long i) {
        return i<0 || i>25 ? '?' : (char)('A' + i);
    }

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
        String title = getMessage("title.not_found");
        String message = getMessage("user.not_found", new Long[] { 5L });
        String jsonContent = toJson(title, message);
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

    @Test
    public void deleteById_isNoContentWhenDeletionIsSuccessful() throws Exception {
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
        String title = getMessage("title.not_found");
        String message = getMessage("user.not_found", new Long[] { 5L });
        String jsonContent = toJson(title, message);
        mvc.perform(delete("/api/users/5")).andExpect(content().json(jsonContent));
    }

    private LocalDateTime getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return LocalDateTime.parse(DEFAULT_DATE_TIME, formatter);
    }

    private String getMessage(String messageCode, Object[] args) {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        return accessor.getMessage(messageCode, args);
    }

    private String getMessage(String messageCode) {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        return accessor.getMessage(messageCode);
    }

    private String toJson(Long id, String name) throws JsonProcessingException {
        LocalDateTime date = getDate();
        UserDto userDto = new UserDto(id, name, date, date);
        return mapper.writeValueAsString(userDto);
    }

    private String makeUsersJsonOfSize(long size) throws JsonProcessingException {
        LocalDateTime date = getDate();
        List<UserDto> users = new ArrayList<>() {{
            for (long i = 0; i < size; i++) {
                add(new UserDto(i, "User " + getChar(i), date, date));
            }
        }};
        return mapper.writeValueAsString(users);
    }

    private String toJson(String title, String message) throws JsonProcessingException {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(title, message);
        return mapper.writeValueAsString(errorResponseDto);
    }

}
