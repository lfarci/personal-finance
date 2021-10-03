package be.loganfarci.financial.service.api.category.model.exception;

public class TransactionCategoryEntityNotFoundException extends TransactionCategoryEntityException {

    private final String categoryName;

    public TransactionCategoryEntityNotFoundException(String categoryName) {
        super(String.format("No category named \"%s\".", categoryName));
        this.categoryName = categoryName;
    }

    public TransactionCategoryEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.categoryName = "";
    }

    public String getCategoryName() {
        return categoryName;
    }
}
