package be.loganfarci.financial.service.api.users.web;

import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import be.loganfarci.financial.service.api.users.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class UserControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String NOT_FOUND_TITLE = "title.not_found";

    private MessageSourceAccessor messages;

    public UserControllerAdvice(MessageSource messages) {
        this.messages = new MessageSourceAccessor(messages);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserService.NotFound.class)
    public ErrorResponseDto handleUserNotFound(UserService.NotFound e) {
        return new ErrorResponseDto(messages.getMessage(NOT_FOUND_TITLE), e.getMessage());
    }

}
