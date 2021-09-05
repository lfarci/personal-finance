package be.loganfarci.financial.service.api.transaction;

import be.loganfarci.financial.csv.FinancialCSVFileReader;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.csv.model.Transaction;
import be.loganfarci.financial.csv.model.Transactions;
import be.loganfarci.financial.service.api.account.BankAccountService;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import be.loganfarci.financial.service.api.owner.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.Objects;

@Service
public class TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final OwnerService ownerService;
    private final BankAccountService bankAccountService;

    public TransactionService(
            TransactionMapper mapper,
            TransactionRepository transactionRepository,
            OwnerService ownerService,
            BankAccountService bankAccountService
    ) {
        this.transactionMapper = mapper;
        this.transactionRepository = transactionRepository;
        this.ownerService = ownerService;
        this.bankAccountService = bankAccountService;
    }

    @Async
    public void saveTransactionsFrom(byte[] bytes) {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Transactions transactions = FinancialCSVFileReader.read(inputStream);
            for(int i = 0; i < transactions.size(); i++){
                save(transactions.get(i));
                logTransactionLoadingProgress(i, transactions.size());
            }
        } catch (FinancialCSVFormatException e) {
            logger.error("Failed to load transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save(Transaction transaction) {
        Objects.requireNonNull(transaction, "A transaction is required");
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setDate(new Date(transaction.getDate().getTime()));
        transactionEntity.setSender(bankAccountService.save(transaction.getInternalBankAccount()));
        transactionEntity.setRecipient(bankAccountService.save(transaction.getExternalBankAccount()));
        transactionEntity.setAmount(transaction.getAmount());
        transactionEntity.setDescription(transaction.getDescription());
        transactionRepository.save(transactionEntity);
    }

    private void logTransactionLoadingProgress(int index, int size) {
        int progress = (int) ((index + 1) * 100.0f) / size;
        logger.info(String.format("Saving transactions: %d%%", progress));
    }

}
