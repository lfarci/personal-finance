package be.loganfarci.financial.service.api.owner.exception;

public class OwnerAlreadyExistsException extends RuntimeException {
    public OwnerAlreadyExistsException(String name) {
        super(String.format("Owner already exists: %s.", name));
    }
}
