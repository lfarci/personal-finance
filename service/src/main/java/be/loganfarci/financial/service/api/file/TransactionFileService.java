package be.loganfarci.financial.service.api.file;

import be.loganfarci.financial.csv.FinancialCSVFileReader;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.csv.model.Transaction;
import be.loganfarci.financial.csv.model.Transactions;
import be.loganfarci.financial.service.api.account.service.BankAccountService;
import be.loganfarci.financial.service.api.account.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.owner.service.OwnerService;
import be.loganfarci.financial.service.api.owner.model.dto.OwnerDto;
import be.loganfarci.financial.service.api.transaction.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class TransactionFileService {

    private final Logger logger = LoggerFactory.getLogger(TransactionFileService.class);

    private final OwnerService ownerService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final TransactionFileMapper mapper;

    public TransactionFileService(
            OwnerService ownerService,
            BankAccountService bankAccountService,
            TransactionService transactionService,
            TransactionFileMapper mapper
    ) {
        this.ownerService = ownerService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.mapper = mapper;
    }

    @Async
    @Transactional(rollbackOn = Exception.class)
    public void importTransactionsFrom(byte[] bytes) {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Transactions transactions = FinancialCSVFileReader.read(inputStream);
            for (int i = 0; i < transactions.size(); i++) {
                importTransaction(transactions.get(i));
                logTransactionLoadingProgress(i, transactions.size());
            }
        } catch (FinancialCSVFormatException e) {
            logger.error("Failed to load transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void importOwner(BankAccount bankAccount) {
        OwnerDto ownerDto = mapper.readOwnerDtoFrom(bankAccount);
        if (ownerService.existsByName(ownerDto.getName())) {
            logger.debug(ownerDto.getName() + " exists.");
        } else {
            ownerService.save(ownerDto);
            logger.debug("New owner with name " + ownerDto.getName() + " saved.");
        }
    }

    private void importBankAccount(BankAccount bankAccount) {
        BankAccountDto bankAccountDto = mapper.readBankAccountDtoFrom(bankAccount);
        importOwner(bankAccount);
        if (bankAccountService.existsById(bankAccountDto.getId())) {
            logger.debug("Account with id " + bankAccountDto.getId() + " exists.");
        } else if (bankAccountService.existsByIban(bankAccountDto.getIban())) {
            logger.debug("Account with IBAN " + bankAccountDto.getIban() + " exists.");
        } else if (bankAccountService.existsByNameAndOwnerName(bankAccountDto)) {
            logger.debug("Account with name " + bankAccountDto.getName() + " exists.");
        } else {
            bankAccountService.save(bankAccountDto);
            logger.debug("A new account was saved");
        }
    }

    private void importTransaction(Transaction transaction) {
        importBankAccount(transaction.getInternalBankAccount());
        importBankAccount(transaction.getExternalBankAccount());
        transactionService.save(mapper.readTransactionDtoFrom(transaction));
    }

    private void logTransactionLoadingProgress(int index, int size) {
        int progress = (int) ((index + 1) * 100.0f) / size;
        logger.info(String.format("Saving transactions: %d%%", progress));
    }

}
