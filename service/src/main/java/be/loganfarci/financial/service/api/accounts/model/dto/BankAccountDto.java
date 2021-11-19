package be.loganfarci.financial.service.api.accounts.model.dto;

import be.loganfarci.financial.service.api.accounts.model.constraints.ValidIban;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BankAccountDto {

    @Min(0)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    private Long userId;

    @ValidIban
    private String iban;

    private Double balance;

    private Boolean isInternal;

    private String ownerName;

    public BankAccountDto(Long id, String name, Long userId, String iban, Double balance, Boolean isInternal, String ownerName) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.iban = iban;
        this.balance = balance;
        this.isInternal = isInternal;
        this.ownerName = ownerName;
    }

    public BankAccountDto(Long id, String name, Long userId, String iban, Double balance) {
        this(id, name, userId, iban, balance, true, null);
    }

    public BankAccountDto(String name, Long userId, String iban, Double balance) {
        this(null, name, userId, iban, balance);
    }

    public BankAccountDto() {
        this(null, null, null, null, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Boolean hasOwnerName() {
        return ownerName != null;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean isInternal() {
        return isInternal;
    }

    public Boolean isExternal() {
        return !isInternal;
    }

    public void setInternal(Boolean internal) {
        isInternal = internal;
    }

    public BankAccountDto id(Long value) {
        this.id = value;
        return this;
    }

    public BankAccountDto name(String value) {
        this.name = value;
        return this;
    }

    public BankAccountDto userId(Long value) {
        this.userId = value;
        return this;
    }

    public BankAccountDto iban(String value) {
        this.iban = value;
        return this;
    }

    public BankAccountDto balance(Double value) {
        this.balance = value;
        return this;
    }

    public BankAccountDto ownerName(String value) {
        this.ownerName = value;
        return this;
    }

    public BankAccountDto internal(Boolean value) {
        this.isInternal = value;
        return this;
    }
}
