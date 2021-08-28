package be.loganfarci.financial.service.transaction;

import be.loganfarci.financial.service.account.BankAccountEntity;
import be.loganfarci.financial.service.category.TransactionCategoryEntity;
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
    private BankAccountEntity sender;

    @OneToOne
    @JoinColumn(name = "recipient", referencedColumnName = "id")
    private BankAccountEntity recipient;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "name")
    private TransactionCategoryEntity category;

}
