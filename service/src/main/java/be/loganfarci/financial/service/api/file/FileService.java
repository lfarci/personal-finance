package be.loganfarci.financial.service.api.file;

import be.loganfarci.financial.csv.FinancialCSVFileReader;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transactions;
import be.loganfarci.financial.service.api.transaction.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileService {

    private final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final TransactionService transactionService;

    public FileService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Async
    public void loadTransactions(InputStream inputStream) {
        try {
            Transactions transactions = FinancialCSVFileReader.read(inputStream);
            for(int i = 0; i < transactions.size(); i++){
                transactionService.save(transactions.get(i));
                logTransactionLoadingProgress(i, transactions.size());
            }
        } catch (FinancialCSVFormatException e) {
            logger.error("Failed to load transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void logTransactionLoadingProgress(int index, int size) {
        int progress = (int) ((index + 1) * 100.0f) / size;
        logger.info(String.format("Saving transactions: %d%%", progress));
    }

}
