package be.loganfarci.financial.service.api;

import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.persistence.UserRepository;
import be.loganfarci.financial.service.api.users.service.UserService;
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
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private static final String TOO_LONG_NAME = "Lorem ipsum dolor sit amet, consectetur vestibulum.";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService service;

    @Autowired
    private MessageSource messageSource;

    public static char getChar(long i) {
        return i < 0 || i > 25 ? '?' : (char) ('A' + i);
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

    private ResultActions postUserJsonContent(String jsonContent) throws Exception {
        return mvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON)
                .content(jsonContent)
        );
    }

    private ResultActions putUserJsonContent(Long id, String jsonContent) throws Exception {
        return mvc.perform(put("/api/users/" + id)
                .contentType(APPLICATION_JSON)
                .content(jsonContent)
        );
    }

    private String getUserNotFoundJsonContent() throws JsonProcessingException {
        String title = getMessage("title.not_found");
        String message = getMessage("user.not_found", new Long[]{5L});
        return toJson(title, message);
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

    private String toJson(String name) throws JsonProcessingException {
        return toJson((Long) null, name);
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
