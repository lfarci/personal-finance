package be.loganfarci.financial.service.api.transaction.model.exception;

public class TransactionBadRequestException extends TransactionEntityException {
    public TransactionBadRequestException(String message) {
        super(message);
    }
}
