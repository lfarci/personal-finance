package be.loganfarci.financial.csv.model;

import java.util.Date;

public class Transaction {

    private final Date date;
    private final Double amount;
    private final BankAccount sender;
    private final BankAccount recipient;
    private final String description;

    public Transaction(Date date, Double amount, BankAccount sender, BankAccount recipient, String description) {
        this.date = date;
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public BankAccount getSender() {
        return sender;
    }

    public BankAccount getRecipient() {
        return recipient;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", amount=" + amount +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", description='" + description + '\'' +
                '}';
    }
}
