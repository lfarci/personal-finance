package be.loganfarci.financial.service.api.owner.model.exception;

public class OwnerEntityException extends RuntimeException {
    public OwnerEntityException(String message) {
        super(message);
    }
    public OwnerEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
