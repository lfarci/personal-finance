package be.loganfarci.financial.service.api.transaction;

import be.loganfarci.financial.csv.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(null);
    }

}
