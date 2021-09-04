package be.loganfarci.financial.service.api.owner.exception;

public class OwnerEntityConstraintViolationException extends OwnerEntityException {
    public OwnerEntityConstraintViolationException(String message, Throwable cause) {
        super(String.format("Owner constraint violation: %s.", message), cause);
    }
}
