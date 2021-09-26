package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.service.api.owner.dto.OwnerErrorDto;
import be.loganfarci.financial.service.api.owner.exception.OwnerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OwnerControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<OwnerErrorDto> handleOwnerNotFound(OwnerNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(new OwnerErrorDto(
                "Not found.",
                "No owner with name: " + e.getOwnerName()
        ), HttpStatus.NOT_FOUND);
    }

}
