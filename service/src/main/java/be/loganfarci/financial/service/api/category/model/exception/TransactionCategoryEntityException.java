package be.loganfarci.financial.service.api.category.model.exception;

public class TransactionCategoryEntityException extends RuntimeException {
    public TransactionCategoryEntityException(String message) {
        super(message);
    }
    public TransactionCategoryEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
