package be.loganfarci.financial.service.api.accounts;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindBankAccountIT extends BankAccountIT {

    @Test
    public void statusIsOkWhenIdExists() throws Exception {
        findByIdAndUserId(Sample.USER_ID, Sample.ID).andExpect(status().isOk());
    }

    @Test
    public void statusIsNotFoundWhenUserIdDoesNotExist() throws Exception {
        findByIdAndUserId(5L, Sample.ID).andExpect(status().isNotFound());
    }

    @Test
    public void statusIsNotFoundWhenBankAccountIdDoesNotExist() throws Exception {
        findByIdAndUserId(Sample.USER_ID, 5L).andExpect(status().isNotFound());
    }

    @Test
    public void responseJsonContentIsExpectedBankAccountWhenBankAccountIdExists() throws Exception {
        String jsonContent = sampleJsonContent();
        findByIdAndUserId(Sample.USER_ID, Sample.ID).andExpect(content().json(jsonContent));
    }

    @Test
    public void responseJsonContentIsExpectedErrorWhenUserIdDoesNotExist() throws Exception {
        String jsonContent = userNotFoundJsonContent(5L);
        findByIdAndUserId(5L, Sample.ID).andExpect(content().json(jsonContent));
    }

    @Test
    public void responseJsonContentIsExpectedErrorWhenBankAccountIdDoesNotExist() throws Exception {
        String jsonContent = bankAccountNotFoundJsonContent(5L);
        findByIdAndUserId(Sample.USER_ID, 5L).andExpect(content().json(jsonContent));
    }

    @Test
    public void statusIsOkWhenGettingBankAccountsForAUserIdThatExist() throws Exception {
        findByUserId(Sample.USER_ID).andExpect(status().isOk());
    }

    @Test
    public void statusIsNotFoundWhenGettingBankAccountsForAUserIdThatDoesNotExist() throws Exception {
        findByUserId(5L).andExpect(status().isNotFound());
    }

    @Test
    public void responseJsonContentIsExpectedErrorWhenGettingBankAccountsForAUserIdThatDoesNotExist() throws Exception {
        findByUserId(5L).andExpect(content().json(userNotFoundJsonContent(5L)));
    }

    @Test
    public void responseJsonContentIsExpectedBankAccounts() throws Exception {
        findByUserId(Sample.USER_ID).andExpect(content().json(sampleListJsonContent()));
    }

}
