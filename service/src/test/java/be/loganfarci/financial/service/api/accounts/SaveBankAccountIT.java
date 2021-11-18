package be.loganfarci.financial.service.api.accounts;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SaveBankAccountIT extends BankAccountIT {

    protected static class SaveSample extends Sample {
        static final String NAME = "Account B";
        static final String IBAN = "IT15X0300203280649287215782";
    }

    private static BankAccountDto sample() {
        return new BankAccountDto(
                SaveSample.NAME,
                SaveSample.USER_ID,
                SaveSample.IBAN,
                SaveSample.BALANCE
        );
    }

    @Test
    public void statusIsCreatedWhenCreationIsSuccessful() throws Exception {
        save(sample()).andExpect(status().isCreated());
    }

    @Test
    public void statusIsCreatedWhenCreationIsSuccessfulWithoutIBAN() throws Exception {
        save(sample().iban(null)).andExpect(status().isCreated());
    }

    @Test
    public void statusIsCreatedWhenCreationIsSuccessfulWithoutBalance() throws Exception {
        save(sample().balance(null)).andExpect(status().isCreated());
    }

    @Test
    public void bankAccountExistsAfterASuccessfulCreation() throws Exception {
        BankAccountDto response = parseBankAccountFrom(save(sample()).andReturn());
        assertThat(service.existsByUserIdAndBankAccountId(0L, response.getId())).isTrue();
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithEmptyName() throws Exception {
        save(sample().name("")).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithBlankName() throws Exception {
        save(sample().name("     ")).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithATooLongName() throws Exception {
        save(sample().name(TOO_LONG_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithoutAName() throws Exception {
        save(sample().name(null)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithoutAUserId() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", null, "IT15X0300203280649287215782", 0.0);
        String bankAccountsPath = getBankAccountPath(0L);
        String jsonContent = mapper.writeValueAsString(bankAccount);
        mvc.perform(post(bankAccountsPath).contentType(APPLICATION_JSON).content(jsonContent)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithUserIdMismatch() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 1L, "IT15X0300203280649287215782", 0.0);
        String bankAccountsPath = getBankAccountPath(0L);
        String jsonContent = mapper.writeValueAsString(bankAccount);
        mvc.perform(post(bankAccountsPath).contentType(APPLICATION_JSON).content(jsonContent)).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsConflictWhenCreatingAnAccountWithExistingIban() throws Exception {
        save(sample().iban(Sample.IBAN)).andExpect(status().isConflict());
    }

    @Test
    public void statusIsNotFoundWhenCreatingAnAccountWithUserIdThatDoesNotExist() throws Exception {
        save(sample().userId(5L)).andExpect(status().isNotFound());
    }

    @Test
    public void responseContentHasExpectedIdentifierWhenCreationIsSuccessful() throws Exception {
        BankAccountDto response = parseBankAccountFrom(save(sample()).andReturn());
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void responseContentHasExpectedNameWhenCreationIsSuccessful() throws Exception {
        BankAccountDto response = parseBankAccountFrom(save(sample()).andReturn());
        assertThat(response.getName()).isEqualTo(SaveSample.NAME);
    }

    @Test
    public void responseContentHasExpectedUserIdWhenCreationIsSuccessful() throws Exception {
        BankAccountDto response = parseBankAccountFrom(save(sample()).andReturn());
        assertThat(response.getUserId()).isEqualTo(SaveSample.USER_ID);
    }

    @Test
    public void responseContentHasExpectedIBANWhenCreationIsSuccessful() throws Exception {
        BankAccountDto response = parseBankAccountFrom(save(sample()).andReturn());
        assertThat(response.getIban()).isEqualTo(SaveSample.IBAN);
    }

    @Test
    public void responseContentHasExpectedBalanceWhenCreationIsSuccessful() throws Exception {
        BankAccountDto response = parseBankAccountFrom(save(sample()).andReturn());
        assertThat(response.getBalance()).isEqualTo(SaveSample.BALANCE);
    }

    @Test
    public void responseContentHasDefaultBalanceWhenCreationIsSuccessfulWithoutBalance() throws Exception {
        BankAccountDto response = parseBankAccountFrom(save(sample().balance(null)).andReturn());
        assertThat(response.getBalance()).isEqualTo(0.0);
    }

}