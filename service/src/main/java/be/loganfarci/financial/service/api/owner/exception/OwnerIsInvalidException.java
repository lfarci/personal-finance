package be.loganfarci.financial.service.api.owner.exception;

public class OwnerIsInvalidException extends OwnerEntityException {
    public OwnerIsInvalidException(String message) {
        super(String.format("Invalid owner: %s.", message));
    }
}