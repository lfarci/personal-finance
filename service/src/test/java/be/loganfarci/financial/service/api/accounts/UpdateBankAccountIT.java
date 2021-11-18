package be.loganfarci.financial.service.api.accounts;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateBankAccountIT extends BankAccountIT {

    @Test
    public void statusIsNoContentAfterSuccessfulUpdate() throws Exception {
        BankAccountDto bankAccount = new BankAccountDto("Update Account", 0L, "IT15X0300203280649287215782", 0.0);
        updateById(0L, bankAccount).andExpect(status().isCreated());
    }
}
