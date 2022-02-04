package be.loganfarci.financial.service.api.errors.exceptions;

public class ResourceConflict extends RuntimeException {
    public ResourceConflict(String message) {
        super(message);
    }
}
