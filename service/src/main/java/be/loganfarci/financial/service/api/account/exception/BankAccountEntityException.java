package be.loganfarci.financial.service.api.account.exception;

public class BankAccountEntityException extends RuntimeException {
    public BankAccountEntityException(String message) {
        super(message);
    }
    public BankAccountEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
