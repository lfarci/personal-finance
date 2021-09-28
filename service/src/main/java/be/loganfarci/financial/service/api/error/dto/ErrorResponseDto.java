package be.loganfarci.financial.service.api.error.dto;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

public class ErrorResponseDto {

    @NotBlank private final String title;
    @NotBlank private final String message;
    Map<String, String> errors;

    public ErrorResponseDto(String title, String message, Map<String, String> errors) {
        this.title = title;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponseDto(String title, String message) {
        this(title, message, new HashMap<>());
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
