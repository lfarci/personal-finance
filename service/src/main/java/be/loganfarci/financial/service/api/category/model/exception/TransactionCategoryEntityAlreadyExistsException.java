package be.loganfarci.financial.service.api.category.model.exception;

public class TransactionCategoryEntityAlreadyExistsException extends TransactionCategoryEntityException {

    private final String categoryName;

    public TransactionCategoryEntityAlreadyExistsException(String categoryName) {
        super(String.format("Category named \"%s\" already exists.", categoryName));
        this.categoryName = categoryName;
    }

    public TransactionCategoryEntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.categoryName = "";
    }

    public String getCategoryName() {
        return categoryName;
    }
}
