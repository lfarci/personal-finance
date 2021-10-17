package be.loganfarci.financial.service.api.transaction.model.dto;

import be.loganfarci.financial.service.api.account.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.category.model.dto.TransactionCategoryDto;

import javax.validation.constraints.*;
import java.sql.Date;

public class WriteTransactionDto {

    private @Min(0) Long id;
    private @NotNull Date date;
    private @NotNull @Min(0) Long internalBankAccountId;
    private @NotNull @Min(0) Long externalBankAccountId;
    private @NotNull Double amount;
    private String description;
    private @NotBlank @Size(max = 50) String categoryName;

    public WriteTransactionDto(Long id, Date date, Long internalBankAccountId, Long externalBankAccountId, Double amount, String description, String categoryName) {
        this.id = id;
        this.date = date;
        this.internalBankAccountId = internalBankAccountId;
        this.externalBankAccountId = externalBankAccountId;
        this.amount = amount;
        this.description = description;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getInternalBankAccountId() {
        return internalBankAccountId;
    }

    public void setInternalBankAccountId(Long internalBankAccountId) {
        this.internalBankAccountId = internalBankAccountId;
    }

    public Long getExternalBankAccountId() {
        return externalBankAccountId;
    }

    public void setExternalBankAccountId(Long externalBankAccountId) {
        this.externalBankAccountId = externalBankAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
