package be.loganfarci.financial.service.api.category.model.exception;

public class InvalidTransactionCategoryException extends TransactionCategoryEntityException {

    private final String categoryName;

    public InvalidTransactionCategoryException(String categoryName) {
        super(String.format("No category named \"%s\".", categoryName));
        this.categoryName = categoryName;
    }

    public InvalidTransactionCategoryException(String message, Throwable cause) {
        super(message, cause);
        this.categoryName = "";
    }

    public String getCategoryName() {
        return categoryName;
    }
}
