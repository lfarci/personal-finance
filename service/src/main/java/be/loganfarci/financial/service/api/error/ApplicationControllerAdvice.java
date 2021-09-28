package be.loganfarci.financial.service.api.error;

import be.loganfarci.financial.service.api.error.dto.ErrorResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = readValidationErrors(ex);
        return new ErrorResponseDto(
                "Bad request",
                "The request could not be performed because the request is not valid.",
                errors
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponseDto handleDataAccessException(DataIntegrityViolationException e) {
        return new ErrorResponseDto(
                "Conflict",
                "The request could not be performed because of a data integrity violation."
        );
    }

    private static Map<String, String> readValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
