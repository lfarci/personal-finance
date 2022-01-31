package be.loganfarci.financial.service.api.accounts;

import be.loganfarci.financial.service.api.PageDeserializer;
import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import com.fasterxml.jackson.databind.JsonNode;

public class BankAccountsPageDeserializer extends PageDeserializer<BankAccountDto> {

    @Override
    protected BankAccountDto deserialize(JsonNode node) {
        BankAccountDto bankAccount = new BankAccountDto()
            .id(node.get("id").asLong())
            .name(node.get("name").asText())
            .userId(node.get("userId").asLong())
            .balance(node.get("balance").asDouble())
            .internal(node.get("internal").asBoolean());
            if (node.get("iban") != null) {
                bankAccount.iban(node.get("iban").asText());
            }
            if (node.get("ownerName") != null) {
                bankAccount.iban(node.get("ownerName").asText());
            }
        return bankAccount;
    }

}
