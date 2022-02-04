package be.loganfarci.financial.service.api.accounts;

import be.loganfarci.financial.service.api.ResourceIT;
import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.service.BankAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
        static final String OWNER_NAME = null;
        static final Boolean INTERNAL = true;
    }

    protected static final String TOO_LONG_NAME = "Lorem ipsum dolor sit amet, consectetur vestibulum.";

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
        return performGet(getBankAccountPath(userId), 0, 10);
    }

    protected ResultActions save(BankAccountDto bankAccount) throws Exception {
        String bankAccountsPath = getBankAccountPath(bankAccount.getUserId());
        String jsonContent = mapper.writeValueAsString(bankAccount);
        return mvc.perform(post(bankAccountsPath).contentType(APPLICATION_JSON).content(jsonContent));
    }

    protected ResultActions updateById(Long userId, Long bankAccountId, BankAccountDto bankAccount) throws Exception {
        String bankAccountsPath = getBankAccountPath(userId, bankAccountId);
        String jsonContent = mapper.writeValueAsString(bankAccount);
        return mvc.perform(put(bankAccountsPath).contentType(APPLICATION_JSON).content(jsonContent));
    }

    protected ResultActions updateById(Long userId, BankAccountDto bankAccount) throws Exception {
        return updateById(userId, bankAccount.getId(), bankAccount);
    }

    protected ResultActions deleteByIdAndUserId(long userId, long bankAccountId) throws Exception {
        return mvc.perform(delete(getBankAccountPath(userId, bankAccountId)));
    }

    protected String sampleJsonContent() throws JsonProcessingException {
        return toJson(Sample.ID, Sample.NAME, Sample.USER_ID, Sample.IBAN, Sample.BALANCE);
    }

    protected String toJson(long id, String name, long userId, String iban, double balance) throws JsonProcessingException {
        BankAccountDto bankAccount = new BankAccountDto(id, name, userId, iban, balance);
        return mapper.writeValueAsString(bankAccount);
    }

    protected BankAccountDto parseBankAccountFrom(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String content = result.getResponse().getContentAsString();
        return mapper.readValue(content, BankAccountDto.class);
    }

    protected List<BankAccountDto> parseBankAccountsFrom(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        module.addDeserializer(PageImpl.class, new BankAccountsPageDeserializer());
        mapper.registerModule(module);
        String content = result.getResponse().getContentAsString();
        TypeReference<PageImpl<BankAccountDto>> pageTypeReference = new TypeReference<>() {};
        Page<BankAccountDto> accounts = mapper.readValue(content, pageTypeReference);
        return accounts.toList();
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
