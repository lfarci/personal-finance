package be.loganfarci.financial.service.api.accounts;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.service.BankAccountService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateBankAccountIT extends BankAccountIT {

    private static BankAccountDto sample() {
        return new BankAccountDto(
                Sample.ID,
                Sample.NAME,
                Sample.USER_ID,
                Sample.IBAN,
                Sample.BALANCE,
                Sample.INTERNAL,
                Sample.OWNER_NAME
        );
    }

    @Test
    public void statusIsNoContentAfterSuccessfulUpdate() throws Exception {
        updateById(Sample.USER_ID, sample()).andExpect(status().isNoContent());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithEmptyName() throws Exception {
        updateById(Sample.USER_ID, sample().name("")).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithBlankName() throws Exception {
        updateById(Sample.USER_ID, sample().name("    ")).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithTooLongName() throws Exception {
        updateById(Sample.USER_ID, sample().name(TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithoutAName() throws Exception {
        updateById(Sample.USER_ID, sample().name(TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithoutAUserId() throws Exception {
        updateById(Sample.USER_ID, sample().userId(null)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsNotFoundWhenUpdatingAnAccountWithUserIdThatDoesNotExist() throws Exception {
        updateById(5L, sample()).andExpect(status().isNotFound());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithUserIdMismatch() throws Exception {
        updateById(0L, sample().userId(1L)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsNoContentAfterSuccessfulUpdateWithoutIBAN() throws Exception {
        updateById(Sample.USER_ID, sample().iban(null)).andExpect(status().isNoContent());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithEmptyIBAN() throws Exception {
        updateById(Sample.USER_ID, sample().iban("")).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithBlankIBAN() throws Exception {
        updateById(Sample.USER_ID, sample().iban("     ")).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithInvalidIBAN() throws Exception {
        updateById(Sample.USER_ID, sample().iban("invalid")).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsNoContentWhenUpdatingAnAccountWithSameIBAN() throws Exception {
        BankAccountDto bankAccount = service.findByIdAndUserId(0L, 1L);
        updateById(0L, bankAccount).andExpect(status().isNoContent());
    }

    @Test
    public void statusIsConflictWhenUpdatingAnAccountWithExistingIBAN() throws Exception {
        BankAccountDto bankAccount = service.findByIdAndUserId(0L, 1L);
        bankAccount.setIban(Sample.IBAN);
        updateById(0L, bankAccount).andExpect(status().isConflict());
    }

    @Test
    public void statusIsBadRequestWhenUpdatingAnAccountWithoutABalance() throws Exception {
        updateById(Sample.USER_ID, sample().balance(null)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsNotFoundWhenUpdatingAnAccountWithAnUnknownAccountId() throws Exception {
        updateById(Sample.USER_ID, 5L, sample()).andExpect(status().isNotFound());
    }

    @Test
    public void statusIsUnprocessableEntityWhenUpdatingAnExternalAccountToInternal() throws Exception {
        updateById(Sample.USER_ID, Sample.ID, sample().internal(false)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void statusIsUnprocessableEntityWhenUpdatingAnInternalAccountToExternal() throws Exception {
        updateById(Sample.USER_ID, 2L, sample().internal(true)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void statusIsUnprocessableEntityWhenUpdatingAnInternalAccountWithOwnerNameDifferentThanNull() throws Exception {
        BankAccountDto account = sample().internal(true).ownerName("Owner Name");
        updateById(Sample.USER_ID, Sample.ID, account).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void statusIsUnprocessableEntityWhenUpdatingAnExternalAccountWithoutOwnerName() throws Exception {
        BankAccountDto account = sample().internal(false).ownerName(null);
        updateById(Sample.USER_ID, 2L, account).andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void nameIsUpdatedAfterASuccessfulUpdate() throws Exception {
        String newName = "Updated bank account name";
        updateById(Sample.USER_ID, sample().name(newName)).andReturn();
        BankAccountDto bankAccount = service.findByIdAndUserId(Sample.USER_ID, Sample.ID);
        assertThat(bankAccount.getName()).isEqualTo(newName);
    }

    @Test
    public void ibanIsUpdatedAfterASuccessfulUpdate() throws Exception {
        String newIban = "IT15X0300203280649287215782";
        updateById(Sample.USER_ID, sample().iban(newIban)).andReturn();
        BankAccountDto bankAccount = service.findByIdAndUserId(Sample.USER_ID, Sample.ID);
        assertThat(bankAccount.getIban()).isEqualTo(newIban);
    }

    @Test
    public void balanceIsUpdatedAfterASuccessfulUpdate() throws Exception {
        Double newBalance = 23.23;
        updateById(Sample.USER_ID, sample().balance(newBalance)).andReturn();
        BankAccountDto bankAccount = service.findByIdAndUserId(Sample.USER_ID, Sample.ID);
        assertThat(bankAccount.getBalance()).isEqualTo(newBalance);
    }

    @Test
    public void ownerNameIsUpdatedAfterASuccessfulUpdateOfExternalAccount() throws Exception {
        String newOwnerName = "Owner Name";
        BankAccountDto account = sample().internal(false).iban(null).ownerName(newOwnerName);
        updateById(Sample.USER_ID, 2L, account).andReturn();
        BankAccountDto bankAccount = service.findByIdAndUserId(Sample.USER_ID, 2L);
        assertThat(bankAccount.getOwnerName()).isEqualTo(newOwnerName);
    }

    @Test
    public void responseContentIsErrorWhenUpdatingInternalFlag() throws Exception {
        String errorMessage = getMessage(BankAccountService.INTERNAL_FLAG_UPDATE);
        updateById(Sample.USER_ID, sample().internal(false)).andExpect(content().json(unprocessableEntityJsonContent(errorMessage)));
    }

    @Test
    public void responseContentIsErrorWhenUpdatingExternalAccountWithoutOwnerName() throws Exception {
        BankAccountDto account = sample().internal(false).ownerName(null);
        String errorMessage = getMessage(BankAccountService.UPDATE_EXTERNAL_OWNER_NAME);
        updateById(Sample.USER_ID, 2L, account).andExpect(content().json(unprocessableEntityJsonContent(errorMessage)));
    }

    @Test
    public void responseContentIsErrorWhenUpdatingInternalAccountWithOwnerName() throws Exception {
        BankAccountDto account = sample().internal(true).ownerName("Owner Name");
        String errorMessage = getMessage(BankAccountService.UPDATE_INTERNAL_OWNER_NAME);
        updateById(Sample.USER_ID, 0L, account).andExpect(content().json(unprocessableEntityJsonContent(errorMessage)));
    }
}
