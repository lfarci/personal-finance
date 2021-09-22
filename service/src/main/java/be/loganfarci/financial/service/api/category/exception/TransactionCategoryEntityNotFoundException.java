package be.loganfarci.financial.service.api.category.exception;

public class TransactionCategoryEntityNotFoundException extends TransactionCategoryEntityException {
    public TransactionCategoryEntityNotFoundException(String categoryName) {
        super(String.format("No category named \"%s\".", categoryName));
    }

    public TransactionCategoryEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
