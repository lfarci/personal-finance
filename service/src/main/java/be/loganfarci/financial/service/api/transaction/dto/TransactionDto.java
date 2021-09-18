package be.loganfarci.financial.service.api.transaction.dto;

import be.loganfarci.financial.service.api.account.dto.BankAccountDto;
import be.loganfarci.financial.service.api.category.dto.TransactionCategoryDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.sql.Date;

public class TransactionDto {

    private @NotNull @Min(0) Long id;
    private @NotNull Date date;
    private @NotNull BankAccountDto internalBankAccount;
    private @NotNull BankAccountDto externalBankAccount;
    private @NotNull Double amount;
    private String description;
    private @Null TransactionCategoryDto category;

    public TransactionDto(Long id, Date date, BankAccountDto internalBankAccount, BankAccountDto externalBankAccount, Double amount, String description, TransactionCategoryDto category) {
        this.id = id;
        this.date = date;
        this.internalBankAccount = internalBankAccount;
        this.externalBankAccount = externalBankAccount;
        this.amount = amount;
        this.description = description;
        this.category = category;
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

    public BankAccountDto getInternalBankAccount() {
        return internalBankAccount;
    }

    public void setInternalBankAccount(BankAccountDto internalBankAccount) {
        this.internalBankAccount = internalBankAccount;
    }

    public BankAccountDto getExternalBankAccount() {
        return externalBankAccount;
    }

    public void setExternalBankAccount(BankAccountDto externalBankAccount) {
        this.externalBankAccount = externalBankAccount;
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

    public TransactionCategoryDto getCategory() {
        return category;
    }

    public void setCategory(TransactionCategoryDto category) {
        this.category = category;
    }
}
