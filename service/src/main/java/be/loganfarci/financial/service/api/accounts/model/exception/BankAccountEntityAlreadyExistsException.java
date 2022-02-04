package be.loganfarci.financial.service.api.accounts.model.exception;

public class BankAccountEntityAlreadyExistsException extends BankAccountException {
    public BankAccountEntityAlreadyExistsException(String message) {
        super(message);
    }

    public BankAccountEntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
