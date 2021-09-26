package be.loganfarci.financial.service.api.owner.dto;

public class OwnerErrorDto {

    private final String title;
    private final String message;

    public OwnerErrorDto(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
