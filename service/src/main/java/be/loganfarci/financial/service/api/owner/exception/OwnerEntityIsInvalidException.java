package be.loganfarci.financial.service.api.owner.exception;

public class OwnerEntityIsInvalidException extends OwnerEntityException {
    public OwnerEntityIsInvalidException(String message) {
        super(String.format("Invalid owner: %s.", message));
    }
}
