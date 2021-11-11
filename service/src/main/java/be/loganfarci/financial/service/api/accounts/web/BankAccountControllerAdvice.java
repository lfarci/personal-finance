package be.loganfarci.financial.service.api.accounts.web;

import be.loganfarci.financial.service.api.accounts.model.exception.BankAccountEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.accounts.model.exception.BankAccountEntityNotFoundException;
import be.loganfarci.financial.service.api.accounts.service.BankAccountService;
import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import be.loganfarci.financial.service.api.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BankAccountControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BankAccountEntityNotFoundException.class)
    public ErrorResponseDto handleBankAccountNotFound(BankAccountEntityNotFoundException e) {
        return new ErrorResponseDto(
                "Not Found",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BankAccountEntityAlreadyExistsException.class)
    public ErrorResponseDto handleBankAccountAlreadyExists(BankAccountEntityAlreadyExistsException e) {
        return new ErrorResponseDto(
                "Conflict",
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BankAccountService.NotFound.class)
    public ErrorResponseDto handleBankAccountNotFound(BankAccountService.NotFound e) {
        return new ErrorResponseDto("Not Found", e.getMessage());
    }

}
