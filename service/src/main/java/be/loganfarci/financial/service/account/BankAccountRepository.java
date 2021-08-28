package be.loganfarci.financial.service.account;

import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends CrudRepository<BankAccountEntity, Long> {
}
