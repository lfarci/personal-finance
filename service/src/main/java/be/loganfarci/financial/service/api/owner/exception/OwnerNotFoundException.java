package be.loganfarci.financial.service.api.owner.exception;

public class OwnerNotFoundException extends OwnerEntityException {

    private final String ownerName;

    public OwnerNotFoundException(String name) {
        super("No owner with such name: " + name);
        this.ownerName = name;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
