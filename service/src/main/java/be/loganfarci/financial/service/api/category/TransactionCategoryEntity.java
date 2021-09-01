package be.loganfarci.financial.service.api.category;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity(name = "transaction_category")
@Check(constraints = "trim(name) <> '' and name not like parent")
public class TransactionCategoryEntity {

    @Id
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", referencedColumnName = "name")
    private TransactionCategoryEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionCategoryEntity> children;

}
