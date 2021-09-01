package be.loganfarci.financial.service.api.account;

import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends CrudRepository<BankAccountEntity, Long> {
}
