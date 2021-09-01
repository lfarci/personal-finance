package be.loganfarci.financial.service.api.account;

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

}
