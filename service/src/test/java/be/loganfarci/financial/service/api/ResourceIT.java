package be.loganfarci.financial.service.api;

import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public abstract class ResourceIT {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected MessageSource messageSource;

    protected String getMessage(String messageCode, Object[] args) {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        return accessor.getMessage(messageCode, args);
    }

    protected String getMessage(String messageCode) {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        return accessor.getMessage(messageCode);
    }

    protected String badRequestJsonContent(String message) throws JsonProcessingException {
        return errorJsonContent("title.bad_request", message);
    }

    protected String notFoundJsonContent(String message) throws JsonProcessingException {
        return errorJsonContent("title.not_found", message);
    }

    protected String unprocessableEntityJsonContent(String message) throws JsonProcessingException {
        return errorJsonContent("title.unprocessable_entity", message);
    }

    private String errorJsonContent(String titleKey, String message) throws JsonProcessingException {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(getMessage(titleKey), message);
        return mapper.writeValueAsString(errorResponseDto);
    }

}
