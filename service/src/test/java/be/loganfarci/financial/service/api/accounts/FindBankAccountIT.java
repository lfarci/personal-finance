package be.loganfarci.financial.service.api.accounts;

import be.loganfarci.financial.service.api.users.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindBankAccountIT extends BankAccountIT {

    @Test
    public void statusIsOkWhenIdExists() throws Exception {
        findByUserIdAndBankAccountId(Sample.USER_ID, Sample.ID).andExpect(status().isOk());
    }

    @Test
    public void statusIsNotFoundWhenUserIdDoesNotExist() throws Exception {
        findByUserIdAndBankAccountId(5L, Sample.ID).andExpect(status().isNotFound());
    }

    @Test
    public void statusIsNotFoundWhenBankAccountIdDoesNotExist() throws Exception {
        findByUserIdAndBankAccountId(Sample.USER_ID, 5L).andExpect(status().isNotFound());
    }

    @Test
    public void responseJsonContentIsExpectedBankAccountWhenBankAccountIdExists() throws Exception {
        String jsonContent = sampleJsonContent();
        findByUserIdAndBankAccountId(Sample.USER_ID, Sample.ID).andExpect(content().json(jsonContent));
    }

    @Test
    public void responseJsonContentIsExpectedErrorWhenUserIdDoesNotExist() throws Exception {
        String jsonContent = userNotFoundJsonContent(5L);
        findByUserIdAndBankAccountId(5L, Sample.ID).andExpect(content().json(jsonContent));
    }

    @Test
    public void responseJsonContentIsExpectedErrorWhenBankAccountIdDoesNotExist() throws Exception {
        String jsonContent = bankAccountNotFoundJsonContent(5L);
        findByUserIdAndBankAccountId(Sample.USER_ID, 5L).andExpect(content().json(jsonContent));
    }

    @Test
    public void statusIsOkWhenGettingBankAccountsForAUserIdThatExist() throws Exception {
        findAll(Sample.USER_ID).andExpect(status().isOk());
    }

    @Test
    public void statusIsNotFoundWhenGettingBankAccountsForAUserIdThatDoesNotExist() throws Exception {
        findAll(5L).andExpect(status().isNotFound());
    }

    @Test
    public void responseJsonContentIsExpectedErrorWhenGettingBankAccountsForAUserIdThatDoesNotExist() throws Exception {
        findAll(5L).andExpect(content().json(userNotFoundJsonContent(5L)));
    }

    @Test
    public void responseJsonContentIsExpectedBankAccounts() throws Exception {
        findAll(Sample.USER_ID).andExpect(content().json(sampleListJsonContent()));
    }

}
