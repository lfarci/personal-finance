package be.loganfarci.financial.service.api.accounts;

import be.loganfarci.financial.service.api.ResourceIT;
import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.persistence.BankAccountRepository;
import be.loganfarci.financial.service.api.accounts.service.BankAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Sql(scripts = "classpath:accounts/accounts.sql")
@Transactional
public abstract class BankAccountIT extends ResourceIT {

    @Autowired
    protected BankAccountService service;

    protected static class Sample {
        static final Long ID = 0L;
        static final String NAME = "Account A";
        static final String IBAN = "NL46INGB4987790602";
        static final Double BALANCE = 237.45;
        static final Long USER_ID = 0L;
    }

    protected String getBankAccountPath(long userId, long bankAccountId) {
        return String.format("/api/users/%d/accounts/%d", userId, bankAccountId);
    }

    protected String getBankAccountPath(long userId) {
        return String.format("/api/users/%d/accounts", userId);
    }

    protected ResultActions findByIdAndUserId(long userId, long bankAccountId) throws Exception {
        return mvc.perform(get(getBankAccountPath(userId, bankAccountId)));
    }

    protected ResultActions findByUserId(long userId) throws Exception {
        return mvc.perform(get(getBankAccountPath(userId)));
    }

    protected ResultActions deleteByIdAndUserId(long userId, long bankAccountId) throws Exception {
        return mvc.perform(delete(getBankAccountPath(userId, bankAccountId)));
    }

    protected String sampleJsonContent() throws JsonProcessingException {
        return toJson(Sample.ID, Sample.NAME, Sample.USER_ID, Sample.IBAN, Sample.BALANCE);
    }

    protected String sampleListJsonContent() throws JsonProcessingException {
        BankAccountDto sample = new BankAccountDto(Sample.ID, Sample.NAME, Sample.USER_ID, Sample.IBAN, Sample.BALANCE);
        return mapper.writeValueAsString(new ArrayList<>(List.of(sample)));
    }

    protected String toJson(long id, String name, long userId, String iban, double balance) throws JsonProcessingException {
        BankAccountDto bankAccount = new BankAccountDto(id, name, userId, iban, balance);
        return mapper.writeValueAsString(bankAccount);
    }

    protected String userNotFoundJsonContent(long userId) throws JsonProcessingException {
        String message = getMessage("user.not_found", new Long[]{ userId });
        return notFoundJsonContent(message);
    }

    protected String bankAccountNotFoundJsonContent(long id) throws JsonProcessingException {
        String message = getMessage("account.not_found", new Long[]{ id });
        return notFoundJsonContent(message);
    }

}
