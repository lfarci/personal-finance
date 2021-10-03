package be.loganfarci.financial.service.api.transaction;

import be.loganfarci.financial.service.api.account.persistence.BankAccountEntity;
import be.loganfarci.financial.service.api.account.service.BankAccountService;
import be.loganfarci.financial.service.api.account.model.exception.BankAccountEntityNotFoundException;
import be.loganfarci.financial.service.api.category.persistence.TransactionCategoryEntity;
import be.loganfarci.financial.service.api.category.service.TransactionCategoryService;
import be.loganfarci.financial.service.api.owner.service.OwnerService;
import be.loganfarci.financial.service.api.transaction.dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final OwnerService ownerService;
    private final BankAccountService bankAccountService;
    private final TransactionCategoryService transactionCategoryService;

    public TransactionService(
            TransactionRepository transactionRepository,
            OwnerService ownerService,
            BankAccountService bankAccountService,
            TransactionCategoryService transactionCategoryService
    ) {
        this.transactionRepository = transactionRepository;
        this.ownerService = ownerService;
        this.bankAccountService = bankAccountService;
        this.transactionCategoryService = transactionCategoryService;
    }

    private boolean hasExistingInternalBankAccount(TransactionDto transaction) {
        return bankAccountService.exists(transaction.getInternalBankAccount());
    }

    private BankAccountEntity findInternalBankAccount(TransactionDto transaction) {
        return bankAccountService.findEntity(transaction.getInternalBankAccount());
    }

    private boolean hasExistingExternalBankAccount(TransactionDto transaction) {
        return bankAccountService.exists(transaction.getExternalBankAccount());
    }

    private BankAccountEntity findExternalBankAccount(TransactionDto transaction) {
        return bankAccountService.findEntity(transaction.getExternalBankAccount());
    }

    public TransactionEntity save(TransactionDto transaction) {
        TransactionEntity entity;
        BankAccountEntity internalBankAccount;
        BankAccountEntity externalBankAccount;
        TransactionCategoryEntity category = null;

        if (transaction == null) {
            throw new IllegalArgumentException("A transaction is required.");
        }

        if (transaction.getInternalBankAccount() == null) {
            throw new IllegalArgumentException("An internal bank account is required.");
        }

        if (transaction.getExternalBankAccount() == null) {
            throw new IllegalArgumentException("An external bank account is required.");
        }

        if (hasExistingInternalBankAccount(transaction)) {
            internalBankAccount = findInternalBankAccount(transaction);
        } else {
            throw new BankAccountEntityNotFoundException("Internal bank account not found.");
        }

        if (hasExistingExternalBankAccount(transaction)) {
            externalBankAccount = findExternalBankAccount(transaction);
        } else {
            throw new BankAccountEntityNotFoundException("External bank account not found.");
        }

        if (transactionCategoryService.exists(transaction.getCategory())) {
            category = transactionCategoryService.find(transaction.getCategory());
        }

        entity = new TransactionEntity();

        entity.setDate(transaction.getDate());
        entity.setInternalBankAccount(internalBankAccount);
        entity.setExternalBankAccount(externalBankAccount);
        entity.setAmount(transaction.getAmount());
        entity.setDescription(transaction.getDescription());
        entity.setCategory(category);

        return transactionRepository.save(entity);
    }

}
