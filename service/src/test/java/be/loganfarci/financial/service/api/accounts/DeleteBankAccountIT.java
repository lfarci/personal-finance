package be.loganfarci.financial.service.api.accounts;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteBankAccountIT extends BankAccountIT {

    @Test
    public void statusIsNoContentWhenDeletionIsSuccessful() throws Exception {
        deleteByIdAndUserId(Sample.USER_ID, Sample.ID).andExpect(status().isNoContent());
    }

    @Test
    public void statusIsNotFoundWhenUserIdDoesNotExist() throws Exception {
        deleteByIdAndUserId(5L, Sample.ID).andExpect(status().isNotFound());
    }

    @Test
    public void statusIsNotFoundWhenBankAccountIdDoesNotExist() throws Exception {
        deleteByIdAndUserId(Sample.USER_ID, 5L).andExpect(status().isNotFound());
    }

    @Test
    public void responseJsonContentIsExpectedErrorWhenUserIdDoesNotExist() throws Exception {
        String jsonContent = userNotFoundJsonContent(5L);
        deleteByIdAndUserId(5L, Sample.ID).andExpect(content().json(jsonContent));
    }

    @Test
    public void responseJsonContentIsExpectedErrorWhenBankAccountIdDoesNotExist() throws Exception {
        String jsonContent = bankAccountNotFoundJsonContent(5L);
        deleteByIdAndUserId(Sample.USER_ID, 5L).andExpect(content().json(jsonContent));
    }

    @Test
    public void bankAccountIsDeletedWhenDeletionIsSuccessful() throws Exception {
        deleteByIdAndUserId(Sample.USER_ID, Sample.ID).andReturn();
        assertThat(service.existsByUserIdAndBankAccountId(Sample.USER_ID, Sample.ID)).isFalse();
    }

}
