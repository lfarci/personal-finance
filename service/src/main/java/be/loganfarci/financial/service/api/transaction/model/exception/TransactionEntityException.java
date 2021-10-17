package be.loganfarci.financial.service.api.transaction.model.exception;

public class TransactionEntityException extends RuntimeException {
    public TransactionEntityException(String message) {
        super(message);
    }
}
