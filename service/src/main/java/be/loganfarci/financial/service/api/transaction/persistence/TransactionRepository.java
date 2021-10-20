package be.loganfarci.financial.service.api.transaction.persistence;

import be.loganfarci.financial.service.api.account.persistence.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM transaction t WHERE t.internalBankAccount = :bankAccount OR t.externalBankAccount = :bankAccount")
    void deleteAllForBankAccount(@Param("bankAccount") BankAccountEntity bankAccount);

}
