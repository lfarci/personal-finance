package be.loganfarci.financial.service.api.transaction.web;

import be.loganfarci.financial.service.api.error.dto.ErrorResponseDto;
import be.loganfarci.financial.service.api.owner.model.exception.OwnerNotFoundException;
import be.loganfarci.financial.service.api.transaction.model.exception.TransactionBadRequestException;
import be.loganfarci.financial.service.api.transaction.model.exception.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TransactionControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TransactionNotFoundException.class)
    public ErrorResponseDto handleTransactionNotFound(TransactionNotFoundException e) {
        return new ErrorResponseDto(
                "Not Found",
                "No transaction with id: " + e.getId()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransactionBadRequestException.class)
    public ErrorResponseDto handleTransactionBadRequest(TransactionBadRequestException e) {
        return new ErrorResponseDto("Bad Request", e.getMessage());
    }

}
