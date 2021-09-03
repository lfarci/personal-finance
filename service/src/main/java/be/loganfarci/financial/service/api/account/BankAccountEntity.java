package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity(name = "bank_account")
@Check(constraints = "trim(name) <> ''")
public class BankAccountEntity {

    @Id
    @GeneratedValue(generator = "account_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "account_sequence_generator",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "owner", referencedColumnName = "name")
    private OwnerEntity owner;

    @Column(name = "iban", length = 34)
    private String iban;

    @Column(name = "balance")
    private Double balance;

    public BankAccountEntity(String name, OwnerEntity owner, String iban, Double balance) {
        this.id = 0L;
        this.name = name;
        this.owner = owner;
        this.iban = iban;
        this.balance = balance;
    }

    public BankAccountEntity() {
        this(null, null, null, null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OwnerEntity getOwner() {
        return owner;
    }

    public String getIban() {
        return iban;
    }

    public Double getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(OwnerEntity owner) {
        this.owner = owner;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
