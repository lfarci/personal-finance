package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.service.api.account.dto.BankAccountDto;
import be.loganfarci.financial.service.api.account.exception.BankAccountIsInvalidException;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import be.loganfarci.financial.service.api.owner.OwnerService;
import be.loganfarci.financial.service.api.owner.dto.OwnerDto;
import be.loganfarci.financial.service.api.owner.exception.OwnerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static be.loganfarci.financial.service.api.account.BankAccountService.REQUIRED_BANK_ACCOUNT_ERROR;
import static be.loganfarci.financial.service.api.account.BankAccountService.REQUIRED_BANK_ACCOUNT_OWNER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTests {

    @Mock
    BankAccountValidator bankAccountValidator;

    @Mock
    BankAccountRepository bankAccountRepository;

    @Mock
    OwnerService ownerService;

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenSavingNull() {
        assertErrorFor(null, IllegalArgumentException.class, REQUIRED_BANK_ACCOUNT_ERROR);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenSavingAccountWithoutOwner() {
        assertErrorFor(bankAccountWithOwner(null), IllegalArgumentException.class, REQUIRED_BANK_ACCOUNT_OWNER_ERROR);
    }

    @Test
    public void shouldThrowOwnerNotFoundExceptionWhenSavingAccountWithUnknownOwner() {
        arrangeOwnerService(false, null);
        assertErrorFor(bankAccountWithOwner(new OwnerDto("Name")), OwnerNotFoundException.class);
    }

    @Test
    public void shouldThrowBankAccountIsInvalidExceptionWhenSavingInvalidAccount() {
        BankAccountDto bankAccount = bankAccount();
        arrangeOwnerService(true, new OwnerEntity("Name"));
        doThrow(BankAccountIsInvalidException.class).when(bankAccountValidator).requireValid(bankAccount);
        assertErrorFor(bankAccount, BankAccountIsInvalidException.class);
    }

    @Test
    public void shouldUseExistingBankAccountWhenSavingExistingAccount() {
        String bankAccountName = "Random account name";
        OwnerEntity owner = new OwnerEntity("Random owner name");
        BankAccountDto bankAccount = bankAccount(bankAccountName);
        BankAccountEntity entity =  bankAccountEntity();
        arrangeOwnerService(true, owner);
        arrangeBankAccountRepository(true, entity);
        BankAccountService service = getService();
        service.save(bankAccount);
        assertThat(entity.getName()).isEqualTo(bankAccountName);
        assertThat(entity.getOwner().getName()).isEqualTo(owner.getName());
    }

    @Test
    public void shouldReturnRepositorySaveResultWhenSaving() {
        BankAccountEntity entity = bankAccountEntity();
        arrangeOwnerService(true, new OwnerEntity("Owner name"));
        arrangeBankAccountRepository(false, entity);
        BankAccountService service = getService();
        assertThat(service.save(bankAccount())).isSameAs(entity);
    }

    private void arrangeOwnerService(boolean exists, OwnerEntity entity) {
        lenient().when(ownerService.existsByName(anyString())).thenReturn(exists);
        lenient().when(ownerService.findByName(anyString())).thenReturn(entity);
    }

    private void arrangeBankAccountRepository(boolean exists, BankAccountEntity entity) {
        Optional<BankAccountEntity> optional = Optional.of(entity);
        lenient().when(bankAccountRepository.existsByNameAndOwnerName(anyString(), anyString())).thenReturn(exists);
        lenient().when(bankAccountRepository.findByNameAndOwnerName(anyString(), anyString())).thenReturn(optional);
        lenient().when(bankAccountRepository.save(any())).thenReturn(entity);
    }

    private BankAccountDto bankAccountWithOwner(OwnerDto owner) {
        return bankAccount("Unknown", owner, "BE82957211769368", 0.0);
    }

    private BankAccountDto bankAccount() {
        return bankAccount("Unknown", new OwnerDto("Name"), "BE82957211769368", 0.0);
    }

    private BankAccountDto bankAccount(String name) {
        return bankAccount(name, new OwnerDto("Name"), "BE82957211769368", 0.0);
    }

    private BankAccountDto bankAccount(String name, OwnerDto owner, String iban, Double balance) {
        return new BankAccountDto(name, owner, iban, balance);
    }

    private BankAccountEntity bankAccountEntity() {
        return bankAccountEntity("Unknown", null, "BE82957211769368", 0.0);
    }

    private BankAccountEntity bankAccountEntity(String name, OwnerEntity owner, String iban, Double balance) {
        return new BankAccountEntity(name, owner, iban, balance);
    }

    private BankAccountService getService() {
        return new BankAccountService(bankAccountValidator, bankAccountRepository, ownerService);
    }

    private void assertErrorFor(BankAccountDto bankAccount, Class<?> exceptionClass) {
        assertThatThrownBy(() -> {
            BankAccountService service = getService();
            service.save(bankAccount);
        }).isInstanceOf(exceptionClass);
    }

    private void assertErrorFor(BankAccountDto bankAccount, Class<?> exceptionClass, String message) {
        assertThatThrownBy(() -> {
            BankAccountService service = getService();
            service.save(bankAccount);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(message);
    }

}
