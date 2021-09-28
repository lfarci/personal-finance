package be.loganfarci.financial.service.api.owner.web;

import be.loganfarci.financial.service.api.error.dto.ErrorResponseDto;
import be.loganfarci.financial.service.api.owner.model.exception.OwnerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class OwnerControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OwnerNotFoundException.class)
    public ErrorResponseDto handleOwnerNotFound(OwnerNotFoundException e) {
        return new ErrorResponseDto(
                "Not Found",
                "No owner with name: " + e.getOwnerName()
        );
    }

}
