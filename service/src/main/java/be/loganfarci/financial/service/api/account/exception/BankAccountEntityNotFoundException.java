package be.loganfarci.financial.service.api.account.exception;

public class BankAccountEntityNotFoundException extends BankAccountEntityException {
    public BankAccountEntityNotFoundException(String message) {
        super(message);
    }
    public BankAccountEntityNotFoundException(String name, String ownerName) {
        super(String.format("No bank owner named \"%s\" for owner \"%s\".", name, ownerName));
    }
    public BankAccountEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
