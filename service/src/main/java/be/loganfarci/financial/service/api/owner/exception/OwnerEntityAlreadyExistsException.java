package be.loganfarci.financial.service.api.owner.exception;

public class OwnerEntityAlreadyExistsException extends RuntimeException {
    public OwnerEntityAlreadyExistsException(String name) {
        super(String.format("Owner already exists: %s.", name));
    }
}
