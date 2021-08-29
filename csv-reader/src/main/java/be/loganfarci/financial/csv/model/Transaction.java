package be.loganfarci.financial.csv.model;

import java.util.Date;

public class Transaction {

    private final Date date;
    private final Double amount;
    private final BankAccount internalBankAccount;
    private final BankAccount externalBankAccount;
    private final String description;

    public Transaction(Date date, Double amount, BankAccount internalBankAccount, BankAccount externalBankAccount, String description) {
        this.date = date;
        this.amount = amount;
        this.internalBankAccount = internalBankAccount;
        this.externalBankAccount = externalBankAccount;
        this.description = description;
    }

    public Transaction(Date date, Double amount, BankAccount externalBankAccount) {
        this(date, amount, null, externalBankAccount, null);
    }

    public Date getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public BankAccount getInternalBankAccount() {
        return internalBankAccount;
    }

    public BankAccount getExternalBankAccount() {
        return externalBankAccount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", amount=" + amount +
                ", internal=" + internalBankAccount +
                ", external=" + externalBankAccount +
                ", description='" + description + '\'' +
                '}';
    }
}
