package be.loganfarci.financial.service.transaction;

import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {
}
