package be.loganfarci.financial.service.api.category.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategoryEntity, String> {
}
