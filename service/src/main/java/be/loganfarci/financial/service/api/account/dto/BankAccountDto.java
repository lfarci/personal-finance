package be.loganfarci.financial.service.api.account.dto;

import be.loganfarci.financial.service.api.owner.dto.OwnerDto;

public class BankAccountDto {

    private String name;
    private OwnerDto owner;
    private String iban;
    private Double balance;

    public BankAccountDto(String name, OwnerDto owner, String iban, Double balance) {
        this.name = name;
        this.owner = owner;
        this.iban = iban;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OwnerDto getOwner() {
        return owner;
    }

    public void setOwner(OwnerDto owner) {
        this.owner = owner;
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

}
