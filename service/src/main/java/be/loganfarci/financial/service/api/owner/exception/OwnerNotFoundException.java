package be.loganfarci.financial.service.api.owner.exception;

public class OwnerNotFoundException extends OwnerEntityException {
    public OwnerNotFoundException(String name) {
        super("No owner with such name: " + name);
    }
}
