package be.loganfarci.financial.service.api.users;

import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.persistence.UserRepository;
import be.loganfarci.financial.service.api.users.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:users/application-test.yml")
@ActiveProfiles("test")
@Sql(scripts = "classpath:users/users.sql")
@Transactional
public abstract class UserIT {

    protected static final String USERS_PATH = "/api/users";

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_TIME = "2021-01-01 00:00:00";

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected UserRepository repository;

    @Autowired
    protected UserService service;

    @Autowired
    protected MessageSource messageSource;

    public static char getChar(long i) {
        return i < 0 || i > 25 ? '?' : (char) ('A' + i);
    }

    public static String getUserPathWithId(long id) {
        return String.format("%s/%d", USERS_PATH, id);
    }

    protected ResultActions postUserJsonContent(String jsonContent) throws Exception {
        return mvc.perform(post(USERS_PATH).contentType(APPLICATION_JSON).content(jsonContent)
        );
    }

    protected ResultActions putUserJsonContent(Long id, String jsonContent) throws Exception {
        return mvc.perform(put(getUserPathWithId(id))
                .contentType(APPLICATION_JSON)
                .content(jsonContent)
        );
    }

    protected String getUserNotFoundJsonContent() throws JsonProcessingException {
        String title = getMessage("title.not_found");
        String message = getMessage("user.not_found", new Long[]{5L});
        return toJson(title, message);
    }

    protected LocalDateTime getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return LocalDateTime.parse(DEFAULT_DATE_TIME, formatter);
    }

    protected String getMessage(String messageCode, Object[] args) {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        return accessor.getMessage(messageCode, args);
    }

    protected String getMessage(String messageCode) {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        return accessor.getMessage(messageCode);
    }

    protected String toJson(Long id, String name) throws JsonProcessingException {
        LocalDateTime date = getDate();
        UserDto userDto = new UserDto(id, name, date, date);
        return mapper.writeValueAsString(userDto);
    }

    protected String toJson(String name) throws JsonProcessingException {
        return toJson((Long) null, name);
    }

    protected String makeUsersJsonOfSize(long size) throws JsonProcessingException {
        LocalDateTime date = getDate();
        List<UserDto> users = new ArrayList<>() {{
            for (long i = 0; i < size; i++) {
                add(new UserDto(i, "User " + getChar(i), date, date));
            }
        }};
        return mapper.writeValueAsString(users);
    }

    protected String toJson(String title, String message) throws JsonProcessingException {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(title, message);
        return mapper.writeValueAsString(errorResponseDto);
    }

}
