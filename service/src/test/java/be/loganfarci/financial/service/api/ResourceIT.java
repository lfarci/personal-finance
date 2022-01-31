package be.loganfarci.financial.service.api;

import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public abstract class ResourceIT {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    protected SimpleModule module = new SimpleModule();

    @Autowired
    protected MessageSource messageSource;

    protected ResultActions performGet(String path, Integer page, Integer size) throws Exception {
        return mvc.perform(get(path)
                .param("page", page.toString())
                .param("size", size.toString()));
    }

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
