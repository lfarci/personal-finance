package be.loganfarci.financial.service.api.users;

import be.loganfarci.financial.service.api.ResourceIT;
import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.persistence.UserRepository;
import be.loganfarci.financial.service.api.users.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Sql(scripts = "classpath:users/users.sql")
@Transactional
public abstract class UserIT extends ResourceIT {

    protected static final String USERS_PATH = "/api/users";
    protected static final long FIRST_EXISTING_USER_ID = 0L;
    protected static final long LAST_EXISTING_USER_ID = 4L;
    protected static final long NEXT_USER_ID = 5L;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_TIME = "2021-01-01 00:00:00";

    protected static final String VALID_NAME = "Roger";
    protected static final String TOO_LONG_NAME = "Lorem ipsum dolor sit amet, consectetur vestibulum.";

    @Autowired
    protected UserRepository repository;

    @Autowired
    protected UserService service;

    public static char getChar(long i) {
        return i < 0 || i > 25 ? '?' : (char) ('A' + i);
    }

    public static String getUserPathWithId(long id) {
        return String.format("%s/%d", USERS_PATH, id);
    }

    protected ResultActions findAll() throws Exception {
        return mvc.perform(get(USERS_PATH));
    }

    protected ResultActions findById(long id) throws Exception {
        return mvc.perform(get(getUserPathWithId(id)));
    }

    protected ResultActions save(String jsonContent) throws Exception {
        return mvc.perform(post(USERS_PATH).contentType(APPLICATION_JSON).content(jsonContent));
    }

    protected ResultActions updateById(Long id, String jsonContent) throws Exception {
        return mvc.perform(put(getUserPathWithId(id))
                .contentType(APPLICATION_JSON)
                .content(jsonContent)
        );
    }

    protected final String notFoundJsonContent(long id) throws JsonProcessingException {
        return notFoundJsonContent(getMessage("user.not_found", new Long[]{ id }));
    }

    protected final String requiredIdErrorJsonContent() throws JsonProcessingException {
        return badRequestJsonContent(getMessage("user.required_id"));
    }

    protected final String idMismatchErrorJsonContent(long bodyUserId, long queryParamUserId) throws JsonProcessingException {
        String message = getMessage("user.id_mismatch", new Long[] { bodyUserId, queryParamUserId });
        return badRequestJsonContent(message);

    }

    protected LocalDateTime getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return LocalDateTime.parse(DEFAULT_DATE_TIME, formatter);
    }

    protected String userJsonContent(Long id, String firstName, String lastName) throws JsonProcessingException {
        LocalDateTime date = getDate();
        UserDto userDto = new UserDto(id, firstName, lastName, date, date);
        return mapper.writeValueAsString(userDto);
    }

    protected String userJsonContent(Long id, String firstName) throws JsonProcessingException {
        return userJsonContent(id, firstName, "lastName");
    }

    protected String userJsonContent(String firstName) throws JsonProcessingException {
        return userJsonContent((Long) null, firstName);
    }

    protected String userJsonContent(String firstName, String lastName) throws JsonProcessingException {
        return userJsonContent(null, firstName, lastName);
    }

    protected String makeUsersJsonOfSize(long size) throws JsonProcessingException {
        LocalDateTime date = getDate();
        List<UserDto> users = new ArrayList<>() {{
            for (long i = 0; i < size; i++) {
                String name = "User " + getChar(i);
                add(new UserDto(i, name, name, date, date));
            }
        }};
        return mapper.writeValueAsString(users);
    }

}
