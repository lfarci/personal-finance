package be.loganfarci.financial.service.api.transaction;

import be.loganfarci.financial.service.api.account.BankAccountEntity;
import be.loganfarci.financial.service.api.category.TransactionCategoryEntity;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "transaction")
@Check(constraints = "date <= current_date and sender <> recipient")
public class TransactionEntity {

    @Id
    @GeneratedValue(generator = "transaction_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "transaction_sequence_generator",
            sequenceName = "transaction_sequence",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private BankAccountEntity internalBankAccount;

    @OneToOne
    @JoinColumn(name = "recipient", referencedColumnName = "id")
    private BankAccountEntity externalBankAccount;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "name")
    private TransactionCategoryEntity category;

    public TransactionEntity(Date date, BankAccountEntity internalBankAccount, BankAccountEntity externalBankAccount, Double amount, String description, TransactionCategoryEntity category) {
        this.id = 0L;
        this.date = date;
        this.internalBankAccount = internalBankAccount;
        this.externalBankAccount = externalBankAccount;
        this.amount = amount;
        this.description = description;
        this.category = category;
    }

    public TransactionEntity(Date date, BankAccountEntity internalBankAccount, BankAccountEntity externalBankAccount, Double amount, String description) {
        this(date, internalBankAccount, externalBankAccount, amount, description, null);
    }

    public TransactionEntity() {
        this(null, null, null, null, null);
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public BankAccountEntity getInternalBankAccount() {
        return internalBankAccount;
    }

    public BankAccountEntity getExternalBankAccount() {
        return externalBankAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public TransactionCategoryEntity getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setInternalBankAccount(BankAccountEntity internalBankAccount) {
        this.internalBankAccount = internalBankAccount;
    }

    public void setExternalBankAccount(BankAccountEntity externalBankAccount) {
        this.externalBankAccount = externalBankAccount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(TransactionCategoryEntity category) {
        this.category = category;
    }
}
