package be.loganfarci.financial.service.api.account.model.dto;

import javax.validation.constraints.*;

public class BankAccountRequestBodyDto {

    @Min(0)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank
    @Size(min = 1, max = 50)
    private String ownerName;

    private String iban;

    @NotNull
    private Double balance;

    public BankAccountRequestBodyDto(Long id, String name, String ownerName, String iban, Double balance) {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.iban = iban;
        this.balance = balance;
    }

    public BankAccountRequestBodyDto() {
        this(0L, null, null,null, null);
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

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
