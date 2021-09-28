package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.service.api.account.persistence.BankAccountEntity;
import be.loganfarci.financial.service.api.account.persistence.BankAccountRepository;
import be.loganfarci.financial.service.api.account.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.account.model.exception.InvalidBankAccountException;
import be.loganfarci.financial.service.api.account.service.BankAccountService;
import be.loganfarci.financial.service.api.account.service.BankAccountValidator;
import be.loganfarci.financial.service.api.owner.persistence.OwnerEntity;
import be.loganfarci.financial.service.api.owner.persistence.OwnerRepository;
import be.loganfarci.financial.service.api.owner.service.OwnerService;
import be.loganfarci.financial.service.api.owner.model.dto.OwnerDto;
import be.loganfarci.financial.service.api.owner.model.exception.OwnerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
public class BankAccountServiceIT {

    private final static String FAR_TOO_LONG_BANK_ACCOUNT_NAME = "Lorem ipsum dolor sit amet, consectetur porta ante.";
    private final static String VALID_BANK_ACCOUNT_OWNER_NAME = "Valid Owner";

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    public void setUp() {
        bankAccountRepository.deleteAll();
        ownerRepository.deleteAll();
        ownerRepository.save(new OwnerEntity(VALID_BANK_ACCOUNT_OWNER_NAME));
    }

    @Test
    public void shouldThrowErrorWhenSavingNull() {
        assertErrorWhenSaving(null, IllegalArgumentException.class);
    }

    @Test
    public void shouldLeaveTableEmptyWhenSavingNull() {
        try {
            saveBankAccount(null);
        } catch (IllegalArgumentException e) {
            assertEmptyBankAccountTable();
        }
    }

    @Test
    public void shouldThrowErrorWhenSavingBankAccountWithoutOwner() {
        BankAccountDto bankAccount = BankAccountDto.with(null);
        assertErrorWhenSaving(bankAccount, IllegalArgumentException.class);
    }

    @Test
    public void shouldLeaveTableEmptyWhenSavingBankAccountWithoutOwner() {
        try {
            saveBankAccount(BankAccountDto.with(null));
        } catch (IllegalArgumentException e) {
            assertEmptyBankAccountTable();
        }
    }

    @Test
    public void shouldThrowErrorWhenSavingBankAccountWithUnknownOwner() {
        ownerRepository.save(new OwnerEntity("An owner"));
        assertErrorWhenSaving(BankAccountDto.with(new OwnerDto("Unknown")), OwnerNotFoundException.class);
    }

    @Test
    public void shouldLeaveTableEmptyWhenSavingBankAccountWithUnknownOwner() {
        try {
            ownerRepository.save(new OwnerEntity("An owner"));
            saveBankAccount(BankAccountDto.with(new OwnerDto("Unknown")));
        } catch (OwnerNotFoundException e) {
            assertEmptyBankAccountTable();
        }
    }

    @Test
    public void shouldThrowErrorWhenSavingBankAccountWithInvalidIban() {
        assertErrorWhenSaving(validBankAccount().iban("Invalid"), InvalidBankAccountException.class);
    }

    @Test
    public void shouldLeaveTableEmptyWhenSavingBankAccountWithInvalidIban() {
        try {
            saveBankAccount(validBankAccount().iban("Invalid"));
        } catch (InvalidBankAccountException e) {
            assertEmptyBankAccountTable();
        }
    }

    @Test
    public void shouldThrowErrorWhenSavingBankAccountWithBlankName() {
        assertErrorWhenSaving(validBankAccount().name(""), InvalidBankAccountException.class);
    }

    @Test
    public void shouldLeaveTableEmptyWhenSavingBankAccountWithBlankName() {
        try {
            saveBankAccount(validBankAccount().name(""));
        } catch (InvalidBankAccountException e) {
            assertEmptyBankAccountTable();
        }
    }

    @Test
    public void shouldThrowErrorWhenSavingBankAccountWithTooLongName() {
        BankAccountDto bankAccount = validBankAccount().name(FAR_TOO_LONG_BANK_ACCOUNT_NAME);
        assertErrorWhenSaving(bankAccount, InvalidBankAccountException.class);
    }

    @Test
    public void shouldLeaveTableEmptyWhenSavingBankAccountWithTooLongName() {
        try {
            saveBankAccount(validBankAccount().name(FAR_TOO_LONG_BANK_ACCOUNT_NAME));
        } catch (InvalidBankAccountException e) {
            assertEmptyBankAccountTable();
        }
    }

    @Test
    public void shouldCreateNewTupleInEmptyTableWhenSavingNewBankAccount() {
        saveBankAccount(validBankAccount());
        assertThat(bankAccountRepository.count()).isEqualTo(1);
    }

    @Test
    public void shouldCreateExpectedTupleInEmptyTableWhenSavingNewBankAccount() {
        BankAccountDto bankAccount = validBankAccount();
        BankAccountEntity entity = saveBankAccount(validBankAccount());
        assertThat(entity.getName()).isEqualTo(bankAccount.getName());
    }

    @Test
    public void shouldNotCreateNewTupleWhenSavingExistingBankAccount() {
        saveBankAccount(validBankAccount());
        saveBankAccount(validBankAccount());
        assertTableRowsCount(1);
    }

    @Test
    public void shouldUpdateBankAccountWhenSavingExistingBankAccount() {
        BankAccountDto bankAccount = validBankAccount().balance(12.7);
        saveBankAccount(bankAccount);
        bankAccount.setBalance(237.0);
        assertTableRowsCount(1);
        assertThat(saveBankAccount(bankAccount).getBalance()).isEqualTo(237.0);
    }

    private OwnerDto validOwner() {
        return new OwnerDto(VALID_BANK_ACCOUNT_OWNER_NAME);
    }

    private BankAccountDto validBankAccount() {
        return BankAccountDto.get().owner(validOwner());
    }

    private void assertTableRowsCount(int count) {
        assertThat(bankAccountRepository.count()).isEqualTo(count);
    }

    private void assertEmptyBankAccountTable() {
        assertTableRowsCount(0);
    }

    private BankAccountEntity saveBankAccount(BankAccountDto bankAccount) {
        BankAccountService service = new BankAccountService(
                new BankAccountValidator(),
                bankAccountRepository,
                new OwnerService(ownerRepository)
        );
        return service.save(bankAccount);
    }

    private void assertErrorWhenSaving(BankAccountDto bankAccount, Class<?> exceptionClass) {
        assertThatThrownBy(() -> {
            saveBankAccount(bankAccount);
        }).isInstanceOf(exceptionClass);
    }

}
