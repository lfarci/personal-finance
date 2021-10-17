package be.loganfarci.financial.service.api.transaction.model.exception;

public class TransactionNotFoundException extends TransactionEntityException {

    private final Long id;

    public TransactionNotFoundException(Long id) {
        super(String.format("No such transaction with given id: %d.", id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
