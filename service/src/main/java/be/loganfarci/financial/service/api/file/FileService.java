package be.loganfarci.financial.service.api.file;

import be.loganfarci.financial.csv.FinancialCSVFileReader;
import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.model.Transaction;
import be.loganfarci.financial.csv.model.Transactions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileService {

    @Async
    public void loadTransactions(InputStream inputStream) {
        try {
            Transactions transactions = FinancialCSVFileReader.read(inputStream);
            for(Transaction transaction : transactions){
                System.out.println(transaction);
            }
        } catch (FinancialCSVFormatException e) {
            System.err.println(e.getMessage());
        }
    }

}
