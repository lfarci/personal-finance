package be.loganfarci.financial.service.api.category.web;

import be.loganfarci.financial.service.api.category.model.exception.InvalidTransactionCategoryException;
import be.loganfarci.financial.service.api.category.model.exception.TransactionCategoryEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.category.model.exception.TransactionCategoryEntityNotFoundException;
import be.loganfarci.financial.service.api.errors.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TransactionCategoryControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TransactionCategoryEntityNotFoundException.class)
    public ErrorResponseDto handleTransactionCategoryNotFound(TransactionCategoryEntityNotFoundException e) {
        return new ErrorResponseDto(
                "Not Found",
                "No transaction category with name: " + e.getCategoryName()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(TransactionCategoryEntityAlreadyExistsException.class)
    public ErrorResponseDto handleTransactionCategoryConflict(TransactionCategoryEntityAlreadyExistsException e) {
        return new ErrorResponseDto(
                "Conflict",
                "Transaction category already exists: " + e.getCategoryName()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTransactionCategoryException.class)
    public ErrorResponseDto handleInvalidTransactionCategory(InvalidTransactionCategoryException e) {
        return new ErrorResponseDto(
                "Bad Request",
                "Invalid transaction category with name: " + e.getCategoryName()
        );
    }

}
