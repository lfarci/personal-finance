package be.loganfarci.financial.service.api.transaction;

import be.loganfarci.financial.csv.FinancialCSVFileReader;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transactions;
import be.loganfarci.financial.service.api.account.BankAccountEntity;
import be.loganfarci.financial.service.api.account.BankAccountService;
import be.loganfarci.financial.service.api.account.exception.BankAccountEntityNotFoundException;
import be.loganfarci.financial.service.api.category.TransactionCategoryEntity;
import be.loganfarci.financial.service.api.category.TransactionCategoryService;
import be.loganfarci.financial.service.api.owner.OwnerService;
import be.loganfarci.financial.service.api.transaction.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

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

    @Async
    public void saveTransactionsFrom(byte[] bytes) {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Transactions transactions = FinancialCSVFileReader.read(inputStream);
            for(int i = 0; i < transactions.size(); i++){
//                save(transactions.get(i));
                logTransactionLoadingProgress(i, transactions.size());
            }
        } catch (FinancialCSVFormatException e) {
            logger.error("Failed to load transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean hasExistingInternalBankAccount(TransactionDto transaction) {
        return bankAccountService.exists(transaction.getInternalBankAccount());
    }

    private BankAccountEntity findInternalBankAccount(TransactionDto transaction) {
        return bankAccountService.find(transaction.getInternalBankAccount());
    }

    private boolean hasExistingExternalBankAccount(TransactionDto transaction) {
        return bankAccountService.exists(transaction.getExternalBankAccount());
    }

    private BankAccountEntity findExternalBankAccount(TransactionDto transaction) {
        return bankAccountService.find(transaction.getExternalBankAccount());
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

    private void logTransactionLoadingProgress(int index, int size) {
        int progress = (int) ((index + 1) * 100.0f) / size;
        logger.info(String.format("Saving transactions: %d%%", progress));
    }

}
