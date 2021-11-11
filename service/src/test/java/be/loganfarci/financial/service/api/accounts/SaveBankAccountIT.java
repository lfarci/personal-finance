package be.loganfarci.financial.service.api.accounts;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SaveBankAccountIT extends BankAccountIT {

    @Test
    public void statusIsCreatedWhenCreationIsSuccessful() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", 0.0);
        save(bankAccount).andExpect(status().isCreated());
    }

    @Test
    public void statusIsCreatedWhenCreationIsSuccessfulWithoutIBAN() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, null, 0.0);
        save(bankAccount).andExpect(status().isCreated());
    }

    @Test
    public void statusIsCreatedWhenCreationIsSuccessfulWithoutBalance() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", null);
        save(bankAccount).andExpect(status().isCreated());
    }

    @Test
    public void bankAccountExistsAfterASuccessfulCreation() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", null);
        BankAccountDto response = parseBankAccountFrom(save(bankAccount).andReturn());
        assertThat(service.existsByUserIdAndBankAccountId(0L, response.getId()));
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithEmptyName() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("", 0L, "IT15X0300203280649287215782", 0.0);
        save(bankAccount).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithBlankName() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("     ", 0L, "IT15X0300203280649287215782", 0.0);
        save(bankAccount).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithATooLongName() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto(TOO_LONG_NAME, 0L, "IT15X0300203280649287215782", 0.0);
        save(bankAccount).andExpect(status().isBadRequest());
    }

    @Test
    public void statusIsBadRequestWhenCreatingAnAccountWithoutAName() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto(null, 0L, "IT15X0300203280649287215782", 0.0);
        save(bankAccount).andExpect(status().isBadRequest());
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
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, Sample.IBAN, 0.0);
        save(bankAccount).andExpect(status().isConflict());
    }

    @Test
    public void statusIsNotFoundWhenCreatingAnAccountWithUserIdThatDoesNotExist() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 5L, Sample.IBAN, 0.0);
        save(bankAccount).andExpect(status().isNotFound());
    }

    @Test
    public void responseContentHasExpectedIdentifierWhenCreationIsSuccessful() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", 0.0);
        BankAccountDto response = parseBankAccountFrom(save(bankAccount).andReturn());
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void responseContentHasExpectedNameWhenCreationIsSuccessful() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", 0.0);
        BankAccountDto response = parseBankAccountFrom(save(bankAccount).andReturn());
        assertThat(response.getName()).isEqualTo("Account B");
    }

    @Test
    public void responseContentHasExpectedUserIdWhenCreationIsSuccessful() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", 0.0);
        BankAccountDto response = parseBankAccountFrom(save(bankAccount).andReturn());
        assertThat(response.getUserId()).isEqualTo(0L);
    }

    @Test
    public void responseContentHasExpectedIBANWhenCreationIsSuccessful() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", 0.0);
        BankAccountDto response = parseBankAccountFrom(save(bankAccount).andReturn());
        assertThat(response.getIban()).isEqualTo("IT15X0300203280649287215782");
    }

    @Test
    public void responseContentHasExpectedBalanceWhenCreationIsSuccessful() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", 23.23);
        BankAccountDto response = parseBankAccountFrom(save(bankAccount).andReturn());
        assertThat(response.getBalance()).isEqualTo(23.23);
    }

    @Test
    public void responseContentHasDefaultBalanceWhenCreationIsSuccessfulWithoutBalance() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Account B", 0L, "IT15X0300203280649287215782", null);
        BankAccountDto response = parseBankAccountFrom(save(bankAccount).andReturn());
        assertThat(response.getBalance()).isEqualTo(0.0);
    }


}
