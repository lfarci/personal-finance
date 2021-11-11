package be.loganfarci.financial.service.api.accounts.web;

import be.loganfarci.financial.service.api.accounts.service.BankAccountService;
import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BankAccountControllerAdvice {

    private static final String NOT_FOUND_TITLE = "title.not_found";
    private static final String CONFLICT_TITLE = "title.conflict";
    private static final String BAD_REQUEST_TITLE = "title.bad_request";

    private MessageSourceAccessor messages;

    public BankAccountControllerAdvice(MessageSource messages) {
        this.messages = new MessageSourceAccessor(messages);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BankAccountService.NotFound.class)
    public ErrorResponseDto handleBankAccountNotFound(BankAccountService.NotFound e) {
        return new ErrorResponseDto(messages.getMessage(NOT_FOUND_TITLE), e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BankAccountService.Conflict.class)
    public ErrorResponseDto handleBankAccountConflict(BankAccountService.Conflict e) {
        return new ErrorResponseDto(messages.getMessage(CONFLICT_TITLE), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BankAccountService.InvalidArgument.class)
    public ErrorResponseDto handleInvalidArgument(BankAccountService.InvalidArgument e) {
        return new ErrorResponseDto(messages.getMessage(BAD_REQUEST_TITLE), e.getMessage());
    }

}
