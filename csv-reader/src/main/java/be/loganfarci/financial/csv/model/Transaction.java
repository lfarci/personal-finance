package be.loganfarci.financial.csv.model;

import java.sql.Date;

public class Transaction {

    private final Date date;
    private final Double amount;
    private final BankAccount sender;
    private final BankAccount recipient;
    private final String transaction;
    private final String communication;

    public Transaction(Date date, Double amount, BankAccount sender, BankAccount recipient, String transaction, String communication) {
        this.date = date;
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.transaction = transaction;
        this.communication = communication;
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

    public String getTransaction() {
        return transaction;
    }

    public String getCommunication() {
        return communication;
    }
}
