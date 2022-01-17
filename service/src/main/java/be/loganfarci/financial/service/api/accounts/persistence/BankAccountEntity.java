package be.loganfarci.financial.service.api.accounts.persistence;

import be.loganfarci.financial.service.api.users.persistence.UserEntity;
import org.hibernate.annotations.Check;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "bank_account")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "owner_name" })
})
@Check(constraints = "trim(name) <> ''")
public class BankAccountEntity {

    public static final int BANK_ACCOUNT_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(generator = "account_sequence_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "account_sequence_generator",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = BANK_ACCOUNT_NAME_LENGTH, nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "application_user", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "iban", length = 34, unique = true)
    private String iban;

    @Column(name = "balance")
    private Double balance;

    /**
     * An internal bank account is owned by one of the application user.
     */
    @Column(name = "is_internal")
    private boolean isInternal;

    public BankAccountEntity(Long id, String name, UserEntity user, String ownerName, String iban, Double balance, boolean isInternal) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.ownerName = ownerName;
        this.iban = iban;
        this.balance = balance;
        this.isInternal = isInternal;
    }

    public BankAccountEntity(String name, String ownerName, String iban, Double balance, boolean isInternal) {
        this(0L, name, null, ownerName, iban, balance, isInternal);
    }

    public BankAccountEntity() {
        this(null, null, null, null, true);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getIban() {
        return iban;
    }

    public Double getBalance() {
        return balance;
    }

    public Boolean hasBalance() {
        return balance != null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }
}
