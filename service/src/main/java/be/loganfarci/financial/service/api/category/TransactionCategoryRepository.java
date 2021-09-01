package be.loganfarci.financial.service.api.category;

import org.springframework.data.repository.CrudRepository;

public interface TransactionCategoryRepository extends CrudRepository<TransactionCategoryEntity, String> {
}
